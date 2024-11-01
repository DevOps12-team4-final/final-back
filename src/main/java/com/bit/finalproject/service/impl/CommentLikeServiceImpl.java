package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.LikeDataDto;
import com.bit.finalproject.dto.NotificationDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.CommentLike;
import com.bit.finalproject.entity.FeedComment;
import com.bit.finalproject.entity.Notification;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.CommentLikeRepository;
import com.bit.finalproject.repository.FeedCommentRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.CommentLikeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentLikeServiceImpl implements CommentLikeService {



    private CommentLikeRepository commentLikeRepository;
    private FeedCommentRepository feedCommentRepository;
    private UserRepository userRepository;
    private NotificationServiceImpl notificationService;
    private  final KafkaTemplate<String, String> kafkaTemplate;


    @Override
    public void likeComment(Long commentId, Long userId) {
        FeedComment feedComment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!commentLikeRepository.existsByFeedCommentAndUser(feedComment, user)) {
            CommentLike commentLike = new CommentLike();
            commentLike.setFeedComment(feedComment);
            commentLike.setUser(user);
            commentLikeRepository.save(commentLike);

            evictLikeCaches(commentId);
        } else {
            throw new IllegalStateException("Comment is already liked by user");
        }
        // kafka 로 보내고 작업
        kafkaTemplate.send("alarm-topic", "%d:%s:%s:%d:"
                .formatted(userId,
                        "COMMENT_LIKE",
                        "like_comment",
                        commentId
                )
        );
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
        evictLikeCaches(commentId);
    }

    @Override
    @Cacheable(value = "likeCount", key = "#commentId")
    public Long getLikeCount(Long commentId) {
        // 댓글 ID로 좋아요 수를 가져옵니다.
        return commentLikeRepository.countByFeedCommentCommentId(commentId);
    }

    @Override
    @Cacheable(value = "likedUsers", key = "#commentId")
    public List<User> getLikedUsers(Long commentId) {
        // 댓글 ID로 해당 댓글을 좋아요한 사용자 목록을 가져옵니다.
        List<CommentLike> commentLikes = commentLikeRepository.findByFeedCommentCommentId(commentId);
        return commentLikes.stream()
                .map(CommentLike::getUser)  // 좋아요한 사용자만 추출
                .collect(Collectors.toList());
    }

    // 좋아요 수와 사용자 목록을 동시에 가져오는 메서드
    @Cacheable(value = "likeData", key = "#commentId")
    public LikeDataDto getLikeCountAndUsers(Long commentId) {
        Long likeCount = commentLikeRepository.countByFeedCommentCommentId(commentId);
        List<UserDto> likedUsers = commentLikeRepository.findByFeedCommentCommentId(commentId)
                .stream()
                .map(commentLike -> {
                    return UserDto.builder()
                            .userId(commentLike.getUser().getUserId())
                            .email(commentLike.getUser().getEmail())
                            .nickname(commentLike.getUser().getNickname())
                            .profileImage(commentLike.getUser().getProfileImage())
                            .build();
                })
                .collect(Collectors.toList());

        return LikeDataDto.builder()
                .likeCount(likeCount)
                .likedUsers(likedUsers)
                .build();
    }

    // 캐시 삭제 메서드
    @CacheEvict(value = {"likeCount", "likedUsers", "likeData"}, key = "#commentId")
    public void evictLikeCaches(Long commentId) {
        // 캐시 자동 삭제
    }



}
