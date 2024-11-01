package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.FeedCommentDto;
import com.bit.finalproject.dto.NotificationDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedComment;
import com.bit.finalproject.repository.FeedCommentRepository;
import com.bit.finalproject.repository.FeedRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.FeedCommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FeedCommentServiceImpl implements FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private NotificationServiceImpl notificationService;
    private  final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @CacheEvict(value = "commentsByFeed", key = "#feedCommentDto.feedId")
    public FeedCommentDto createComment(FeedCommentDto feedCommentDto) {
        // Feed 엔티티 설정
        Feed feed = feedRepository.findById(feedCommentDto.getFeedId())
                .orElseThrow(() -> new IllegalArgumentException("Feed not found with id: " + feedCommentDto.getFeedId()));

        // FeedComment 엔티티 생성
        FeedComment feedComment = new FeedComment();
        feedComment.setComment(feedCommentDto.getComment());
        feedComment.setOrderNumber(0);  // 기본값 0을 명시적으로 설정
        feedComment.setFeed(feed);  // Feed 엔티티 설정

        // User 정보 설정
        feedComment.setUser(userRepository.findById(feedCommentDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"))
        );

        // 부모 댓글이 있는지 확인 후 처리
        if (feedCommentDto.getParentCommentId() != null) {
            // parentCommentId로 부모 댓글 가져오기
            FeedComment parentComment = feedCommentRepository.findById(feedCommentDto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found with id: " + feedCommentDto.getParentCommentId()));

            // 부모 댓글의 depth에 +1
            feedComment.setDepth(parentComment.getDepth() + 1);
            // 부모 댓글 ID 설정
            feedComment.setParentCommentId(parentComment.getCommentId());  // 여기서 부모 댓글의 commentId를 사용
        } else {
            // 원댓글인 경우
            feedComment.setDepth(0);  // 깊이 0 설정
        }

        // 댓글 기본 정보 설정
        feedComment.setRegdate(LocalDateTime.now());
        feedComment.setIsdelete("F"); // 댓글은 처음 생성할 때 삭제 상태가 아님

        // 댓글 저장
        FeedComment savedComment = feedCommentRepository.save(feedComment);
        // kafka 로 보내고 작업
        kafkaTemplate.send("alarm-topic",
                "%d:%s:%s:%d:".formatted(feedCommentDto.getUserId(),
                        "FEED_COMMENT",
                        feedCommentDto.getComment(),
                        savedComment.getCommentId()
                )
        );

        // 필드 값들을 로그로 출력
        log.info("feedComment ID: {}", savedComment.getCommentId());
        log.info("feedComment 작성자 ID: {}", savedComment.getUser().getUserId());

        // 저장된 댓글을 FeedCommentDto로 변환하여 반환
        FeedCommentDto savedCommentDto = new FeedCommentDto();
        savedCommentDto.setFeedId(savedComment.getFeed().getFeedId());  // Feed ID 설정
        savedCommentDto.setCommentId(savedComment.getCommentId());  // Comment ID 설정
        savedCommentDto.setUserId(savedComment.getUser().getUserId());  // User ID 설정
        savedCommentDto.setComment(savedComment.getComment());  // Comment 설정
        savedCommentDto.setRegdate(savedComment.getRegdate());  // 등록일 설정
        savedCommentDto.setModdate(savedComment.getModdate());  // 수정일 설정
        savedCommentDto.setIsdelete(savedComment.getIsdelete());  // 삭제 여부 설정
        savedCommentDto.setDepth(savedComment.getDepth());  // Depth 설정
        savedCommentDto.setParentCommentId(savedComment.getParentCommentId());
        savedCommentDto.setProfileImage(feedCommentDto.getProfileImage());

        // 부모 댓글 ID 설정
        if (savedComment.getParentCommentId() != null) {
            savedCommentDto.setParentCommentId(savedComment.getParentCommentId());
        }


        return savedCommentDto;
    }

    @Override
    public FeedCommentDto findById(Long commentId) {
        // 댓글 ID로 댓글 조회, 없으면 예외 발생
        FeedComment feedComment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // FeedComment 엔티티를 FeedCommentDto로 변환하여 반환
        FeedCommentDto feedCommentDto = new FeedCommentDto();
        feedCommentDto.setCommentId(feedComment.getCommentId());
        feedCommentDto.setComment(feedComment.getComment());
        feedCommentDto.setUserId(feedComment.getUser().getUserId());
        feedCommentDto.setFeedId(feedComment.getFeed().getFeedId());
        feedCommentDto.setRegdate(feedComment.getRegdate());
        feedCommentDto.setModdate(feedComment.getModdate());
        feedCommentDto.setIsdelete(feedComment.getIsdelete());
        feedCommentDto.setDepth(feedComment.getDepth());
        feedCommentDto.setParentCommentId(feedComment.getParentCommentId());
        feedCommentDto.setProfileImage(feedCommentDto.getProfileImage());
        if (feedComment.getParentCommentId() != null) {
            feedCommentDto.setParentCommentId(feedComment.getParentCommentId());
        }
        return feedCommentDto;
    }

    @Override
    @Cacheable(value = "commentsByFeed", key = "#feedId")
    public List<FeedCommentDto> findAllCommentsByFeedId(Long feedId) {
        // Feed 엔티티가 존재하는지 확인
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found with id: " + feedId));

        // feedId에 해당하는 모든 댓글을 가져옴
        List<FeedComment> comments = feedCommentRepository.findByFeed(feed);

        // 댓글 엔티티 리스트를 DTO 리스트로 변환하여 반환
        return comments.stream()
                .map(comment -> {
                    FeedCommentDto commentDto = new FeedCommentDto();
                    commentDto.setCommentId(comment.getCommentId());
                    commentDto.setFeedId(feedId);
                    commentDto.setUserId(comment.getUser().getUserId());
                    commentDto.setComment(comment.getComment());
                    commentDto.setDepth(comment.getDepth());
                    commentDto.setParentCommentId(comment.getParentCommentId());
                    commentDto.setRegdate(comment.getRegdate());
                    commentDto.setModdate(comment.getModdate());
                    commentDto.setIsdelete(comment.getIsdelete());
                    // 프로필 이미지 설정 (user가 null이 아닐 때만)
                    String profileImage = (comment.getUser() != null && comment.getUser().getProfileImage() != null)
                            ? comment.getUser().getProfileImage()
                            : ""; // 기본 이미지 경로 설정
                    commentDto.setProfileImage(profileImage);

                    return commentDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "commentsByFeed", key = "#feedComment.feedId")
    public FeedCommentDto updateComment(Long commentId, FeedCommentDto feedCommentDto) {
        // 댓글 조회
        FeedComment feedComment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 댓글 내용 수정
        feedComment.setComment(feedCommentDto.getComment());
        feedComment.setModdate(LocalDateTime.now());  // 수정일시 설정

        // 저장 후 수정된 댓글을 반환
        FeedComment updatedComment = feedCommentRepository.save(feedComment);

        // FeedComment 엔티티를 FeedCommentDto로 변환하여 반환
        FeedCommentDto updatedCommentDto = new FeedCommentDto();
        updatedCommentDto.setCommentId(updatedComment.getCommentId());
        updatedCommentDto.setComment(updatedComment.getComment());
        updatedCommentDto.setUserId(updatedComment.getUser().getUserId());
        updatedCommentDto.setFeedId(updatedComment.getFeed().getFeedId());
        updatedCommentDto.setRegdate(updatedComment.getRegdate());
        updatedCommentDto.setModdate(updatedComment.getModdate());
        updatedCommentDto.setIsdelete(updatedComment.getIsdelete());
        updatedCommentDto.setDepth(updatedComment.getDepth());
        updatedCommentDto.setParentCommentId(updatedComment.getParentCommentId());
        updatedCommentDto.setProfileImage(updatedCommentDto.getProfileImage());
        if (updatedComment.getParentCommentId() != null) {
            updatedCommentDto.setParentCommentId(updatedComment.getParentCommentId());
        }
        return updatedCommentDto;
    }

    @Override
    @CacheEvict(value = "commentsByFeed", key = "#feedComment.feedId")
    public void deleteComment(Long commentId, Long userId) throws AccessDeniedException, NoSuchElementException {
        FeedComment feedComment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("삭제할 댓글이 존재하지 않습니다."));

        // 댓글 작성자와 현재 사용자가 같은지 확인
        if (!feedComment.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("댓글을 삭제할 권한이 없습니다.");
        }

        // isdelete 값을 "T"로 설정하여 소프트 삭제
        feedComment.setIsdelete("T");
        feedComment.setDeletedate(LocalDateTime.now());
        feedCommentRepository.save(feedComment);

    }
}

