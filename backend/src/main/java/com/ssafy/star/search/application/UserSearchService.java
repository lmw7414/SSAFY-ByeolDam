package com.ssafy.star.search.application;


import com.ssafy.star.search.dao.UserSearchRepository;
import com.ssafy.star.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserSearchService {

    private final UserSearchRepository userSearchRepository;

    public Page<UserEntity> userSearch(String keyword, Pageable pageable) {
        return userSearchRepository.findByNicknameContaining(keyword, pageable);
    }
}
