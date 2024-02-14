package com.ssafy.star.search.application;


import com.ssafy.star.search.dao.UserSearchRepository;
import com.ssafy.star.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserSearchService {

    private final UserSearchRepository userSearchRepository;

    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    public List<UserEntity> userSearch(String keyword) {
        return userSearchRepository.findByNicknameContaining(keyword, sort);
    }
}
