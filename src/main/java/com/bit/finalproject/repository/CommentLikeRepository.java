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

    // 특정 댓글의 좋아요 개수
    Long countByFeedCommentCommentId(Long commentId);

    // 특정 댓글을 좋아요한 사용자 목록
    List<CommentLike> findByFeedCommentCommentId(Long commentId);


}
