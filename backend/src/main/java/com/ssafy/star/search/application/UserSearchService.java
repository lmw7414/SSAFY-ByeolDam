package com.ssafy.star.search.application;


import com.ssafy.star.search.dao.UserSearchRepository;
import com.ssafy.star.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserSearchService {

    private final UserSearchRepository userSearchRepository;

    int pageNumber = 0;
    int pageSize = 5;

    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    @Transactional
    public List<UserEntity> userSearch(String keyword) {
        return userSearchRepository.findByNicknameContaining(keyword, sort);
    }

    @Transactional
    public Page<UserEntity> userRelatedSearch(String keyword) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return userSearchRepository.findAllByNicknameContaining(keyword, pageable);
    }
}
