package com.bit.finalproject.repository;

import java.util.List;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    // 특정 피드에 속한 모든 댓글을 조회하는 메서드
    List<FeedComment> findByFeed(Feed feed);
}
