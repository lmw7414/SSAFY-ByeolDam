package com.ssafy.star.user.application;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.notification.dto.NotificationArgs;
import com.ssafy.star.notification.dto.NotificationEvent;
import com.ssafy.star.notification.dto.NotificationType;
import com.ssafy.star.notification.producer.NotificationProducer;
import com.ssafy.star.user.domain.ApprovalStatus;
import com.ssafy.star.user.domain.FollowEntity;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.dto.Follow;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.repository.FollowRepository;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationProducer notificationProducer;

    public ApprovalStatus requestFollow(String fromUserEmail, String toUserNickname) {
        // 1. 해당 유저가 실제로 존재하는지 확인하기
        UserEntity fromUser = getUserEntityByEmailOrException(fromUserEmail);
        UserEntity toUser = getUserEntityByNicknameOrException(toUserNickname);

        // 나에서 나에게 요청하는 경우
        if(fromUser.equals(toUser)) {
            throw new ByeolDamException(ErrorCode.SELF_FOLLOW_ERROR);
        }

        FollowEntity result = followRepository.findByFromUserAndToUser(fromUser, toUser).orElse(null);
        if (result == null) { // 2-1. 새로 팔로우하는 경우 -> 요청
            if (DisclosureType.VISIBLE.equals(toUser.getDisclosureType())) {
                // 상대의 프로필이 공개인 경우 -> 바로 팔로우 요청 수락(status는 ACCEPT)
                Follow.fromEntity(followRepository.save(
                        FollowEntity.of(fromUser, toUser, LocalDateTime.now(), ApprovalStatus.ACCEPT)
                ));
                notificationProducer.send(new NotificationEvent(NotificationType.FOLLOWED, new NotificationArgs(fromUser.getId(), toUser.getId()), toUser.getId()));
                return ApprovalStatus.ACCEPT;
            } else {
                // 상대의 프로필이 비공개인 경우  -> 팔로우 요청 보내기(status는 REQUEST)
                Follow.fromEntity(followRepository.save(
                        FollowEntity.of(fromUser, toUser, null, ApprovalStatus.REQUEST)
                ));
                notificationProducer.send(new NotificationEvent(NotificationType.FOLLOW_REQUEST, new NotificationArgs(fromUser.getId(), toUser.getId()), toUser.getId()));
                return ApprovalStatus.REQUEST;
            }
        } else {
            // 2-2. 이미 팔로우 상태일 경우 -> 취소
            // 2-3. 팔로우를 보냈지만 아직 ACCEPT가 나지 않은 경우 -> 취소
            followRepository.delete(result);
            return ApprovalStatus.CANCEL;
        }
    }

    public ApprovalStatus followStatus(String fromUserEmail, String toUserNickname) {
        UserEntity fromUser = getUserEntityByEmailOrException(fromUserEmail);
        UserEntity toUser = getUserEntityByNicknameOrException(toUserNickname);
        FollowEntity result = followRepository.findByFromUserAndToUser(fromUser, toUser).orElse(null);
        if (result == null) {
            return ApprovalStatus.NOTHING;
        } else {
            return result.getStatus();
        }

    }
    //팔로우 수락하기
    // 사용자 요청은 CANCEL, ACCEPT만 보내야 함
    public ApprovalStatus acceptFollow(String fromUserEmail, String toUserNickname, ApprovalStatus status) {
        UserEntity fromUser = getUserEntityByEmailOrException(fromUserEmail);
        UserEntity toUser = getUserEntityByNicknameOrException(toUserNickname);

        FollowEntity result = followRepository.findByFromUserAndToUser(toUser, fromUser).orElseThrow(() ->
                new ByeolDamException(ErrorCode.FOLLOW_NOT_FOUND)); // 레포지토리에 해당하는 데이터가 없는 경우

        if (ApprovalStatus.REQUEST.equals(result.getStatus())) {
            if (ApprovalStatus.CANCEL.equals(status)) {
                result.setStatus(ApprovalStatus.CANCEL);
                followRepository.delete(result);
                return ApprovalStatus.CANCEL;
            } else if (ApprovalStatus.ACCEPT.equals(status)) {
                result.setStatus(ApprovalStatus.ACCEPT);
                result.setAcceptDate(LocalDateTime.now());
                followRepository.save(result);
                notificationProducer.send(new NotificationEvent(NotificationType.FOLLOW_ACCEPT, new NotificationArgs(fromUser.getId(), toUser.getId()), toUser.getId()));
                return ApprovalStatus.ACCEPT;
            }
        }
        throw new ByeolDamException(ErrorCode.INVALID_REQUEST);
    }

    //나에게 팔로우를 신청한 사람들의 리스트 보기
    public List<User> requestFollowList(String userEmail) {
        UserEntity user = getUserEntityByEmailOrException(userEmail);
        return followRepository.findFromUsersByToUserAndStatus(user, ApprovalStatus.REQUEST)
                .stream().map(User::fromEntity).toList();
    }

    //내 팔로워 확인하기
    public List<User> followers(String userEmail) {
        UserEntity user = getUserEntityByEmailOrException(userEmail);
        return followRepository.findByToUserAndStatus(user, ApprovalStatus.ACCEPT)
                .stream()
                .map(FollowEntity::getFromUser)
                .map(User::fromEntity)
                .toList();
    }

    //내 팔로잉 확인하기
    public List<User> followings(String userEmail) {
        UserEntity user = getUserEntityByEmailOrException(userEmail);
        List<FollowEntity> result = followRepository.findByFromUserAndStatus(user, ApprovalStatus.ACCEPT);
        log.info("팔로우 엔티티 정보 : {}", result);
        return followRepository.findByFromUserAndStatus(user, ApprovalStatus.ACCEPT)
                .stream()
                .map(FollowEntity::getToUser)
                .map(User::fromEntity)
                .toList();
    }

    //남의 팔로워 확인하기
    public List<User> otherFollowers(String fromUserEmail, String toUserNickname) {
        UserEntity fromUser = getUserEntityByEmailOrException(fromUserEmail);
        UserEntity toUser = getUserEntityByNicknameOrException(toUserNickname);

        FollowEntity result = followRepository.findByFromUserAndToUserAndStatus(fromUser, toUser, ApprovalStatus.ACCEPT).orElse(null);

        if (result == null) {  // 팔로잉 관계가 아닐경우 -> toUser가 비공개일 경우 볼수 없음
            if (DisclosureType.INVISIBLE.equals(toUser.getDisclosureType())) {
                throw new ByeolDamException(ErrorCode.INVALID_PERMISSION);
            }
        }
        return followers(toUser.getEmail());
    }

    //남의 팔로잉 확인하기
    public List<User> otherFollowings(String fromUserEmail, String toUserNickname) {
        UserEntity fromUser = getUserEntityByEmailOrException(fromUserEmail);
        UserEntity toUser = getUserEntityByNicknameOrException(toUserNickname);

        FollowEntity result = followRepository.findByFromUserAndToUserAndStatus(fromUser, toUser, ApprovalStatus.ACCEPT).orElse(null);

        if (result == null) {  // 팔로잉 관계가 아닐경우 -> toUser가 비공개일 경우 볼수 없음
            if (DisclosureType.INVISIBLE.equals(toUser.getDisclosureType())) {
                throw new ByeolDamException(ErrorCode.INVALID_PERMISSION);
            }
        }
        return followings(toUser.getEmail());
    }

    //팔로워 수 반환하기
    public long countFollowers(String nickName) {
        UserEntity user = getUserEntityByNicknameOrException(nickName);
        return followRepository.countByToUserAndStatus(user, ApprovalStatus.ACCEPT);
    }

    //팔로잉 수 반환하기
    public long countFollowings(String nickName) {
        UserEntity user = getUserEntityByNicknameOrException(nickName);
        return followRepository.countByFromUserAndStatus(user, ApprovalStatus.ACCEPT);
    }

    private UserEntity getUserEntityByNicknameOrException(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", nickname)));
    }

    private UserEntity getUserEntityByEmailOrException(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email)));
    }

}
