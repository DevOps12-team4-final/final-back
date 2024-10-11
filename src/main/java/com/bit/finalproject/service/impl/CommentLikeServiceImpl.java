package com.bit.finalproject.service.impl;

import com.bit.finalproject.entity.CommentLike;
import com.bit.finalproject.entity.FeedComment;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.CommentLikeRepository;
import com.bit.finalproject.repository.FeedCommentRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.CommentLikeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {


    private CommentLikeRepository commentLikeRepository;
    private FeedCommentRepository feedCommentRepository;
    private UserRepository userRepository;

    @Override
    public void likeComment(Long commentId, Long userId) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!commentLikeRepository.existsByFeedCommentAndUser(comment, user)) {
            CommentLike commentLike = new CommentLike();
            commentLike.setFeedComment(comment);
            commentLike.setUser(user);
            commentLikeRepository.save(commentLike);
        } else {
            throw new IllegalStateException("Comment is already liked by user");
        }

    }

    @Override
    public void unlikeComment(Long commentId, Long userId) {
        FeedComment feedComment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // 사용자가 해당 댓글에 이미 좋아요를 누른 상태인지 확인
        CommentLike commentLike = commentLikeRepository.findByFeedCommentAndUser(feedComment, user)
                .orElseThrow(() -> new IllegalArgumentException("No like found for this comment and user"));

        // 좋아요 취소
        commentLikeRepository.delete(commentLike);
    }


}
