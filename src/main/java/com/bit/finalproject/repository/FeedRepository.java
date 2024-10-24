package com.bit.finalproject.repository;

import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    // 운동, 댓글 추가해야함
    @EntityGraph(attributePaths = {"user", "feedFileList", "likes"})
    List<Feed> findAll();  // 관련 엔티티들과 함께 모든 게시글을 가져오기

    Page<Feed> findByUser_UserIdNot(Long userId, Pageable pageable);

    Page<Feed> findByUser_UserIdIn(List<Long> followingIdList, Pageable pageable);
}
