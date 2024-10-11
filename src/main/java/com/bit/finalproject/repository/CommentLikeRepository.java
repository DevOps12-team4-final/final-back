package com.bit.finalproject.repository;

import com.bit.finalproject.entity.CommentLike;
import com.bit.finalproject.entity.FeedComment;
import com.bit.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByFeedCommentAndUser(FeedComment feedComment, User user);
    // 댓글 사용자 존재여부

    Optional<CommentLike> findByFeedCommentAndUser(FeedComment feedComment, User user);
    // 댓글 사용자를 찾는


}
