package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedLike;
import com.bit.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    // 특정 게시글 좋아요 개수 반환
    int countByFeed(Feed feed);

    // 특정 게시글과 사용자의 좋아요 여부 확인
    boolean existsByFeedAndUser(Feed feed, User user);

    // 특정 게시글과 사용자의 좋아요 찾기
    Optional<FeedLike> findByFeedAndUser(Feed feed, User user);
}
