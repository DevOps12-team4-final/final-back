package com.bit.finalproject.controller;

import com.bit.finalproject.dto.FeedCommentDto;
import com.bit.finalproject.dto.LikeDataDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.entity.FeedComment;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.service.CommentLikeService;
import com.bit.finalproject.service.FeedCommentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/feed-comment")
@RequiredArgsConstructor
@Slf4j
public class FeedCommentController {

    private final FeedCommentService feedCommentService;


    @PostMapping
    public ResponseEntity<ResponseDto<FeedCommentDto>> createComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody FeedCommentDto feedCommentDto) {

        ResponseDto<FeedCommentDto> response = new ResponseDto<>();

        try {
            if (userDetails == null) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                response.setStatusMessage("Authentication required.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            // 사용자의 User 정보 가져오기
            Long userId = userDetails.getUser().getUserId();  // CustomUserDetails에서 User 가져옴
            feedCommentDto.setUserId(userId);

            // 서비스 레이어에서 댓글 생성 처리
            FeedCommentDto createdComment = feedCommentService.createComment(feedCommentDto);

            response.setItem(createdComment);
            response.setStatusCode(HttpStatus.OK.value());
            response.setStatusMessage("Comment created successfully.");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setStatusMessage("Error occurred while creating comment.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 특정 피드의 모든 댓글을 조회하는 API
    @GetMapping("/feed/{feedId}")
    public ResponseEntity<?> findAllCommentsByFeedId(@PathVariable Long feedId) {
        try {
            List<FeedCommentDto> comments = feedCommentService.findAllCommentsByFeedId(feedId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            // 예외 발생 시 500 Internal Server Error 반환 및 에러 메시지 전달
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 조회 실패: " + e.getMessage());
        }
    }

    // 댓글을 수정하는 API (인증된 사용자만 가능)
    @PutMapping("/feed/comment/update/{commentId}")
    public ResponseEntity<ResponseDto> commentUpdate(@PathVariable Long commentId,
                                                     @RequestBody FeedCommentDto feedCommentDto,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // 댓글을 찾기 위해 기존 댓글을 조회
            FeedCommentDto existingComment = feedCommentService.findById(commentId);

            // 현재 로그인한 사용자가 댓글 작성자인지 확인
            if (!existingComment.getUserId().equals(userDetails.getUser().getUserId())) {
                responseDto.setItem(null);
                responseDto.setStatusMessage("댓글을 수정할 권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
            }

            // 댓글 수정 로직 수행
            FeedCommentDto updatedComment = feedCommentService.updateComment(commentId, feedCommentDto);

            // 성공적으로 업데이트된 경우
            responseDto.setItem(updatedComment);
            responseDto.setStatusMessage("댓글이 성공적으로 업데이트되었습니다.");
            return ResponseEntity.ok(responseDto);

        } catch (IllegalArgumentException e) {
            // 잘못된 댓글 ID 또는 업데이트 불가능한 상황에 대한 예외 처리
            responseDto.setItem(null);
            responseDto.setStatusMessage("댓글을 찾을 수 없거나 업데이트할 수 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);

        } catch (Exception e) {
            // 일반적인 예외 처리
            responseDto.setItem(null);
            responseDto.setStatusMessage("댓글 업데이트 중 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    // 댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDto<String>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ResponseDto<String> responseDto = new ResponseDto<>();
        try {
            Long userId = userDetails.getUser().getUserId(); // 사용자 정보 가져오기
            feedCommentService.deleteComment(commentId, userId); // 서비스 계층에서 삭제 처리
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("댓글이 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(responseDto);
        } catch (AccessDeniedException e) {
            responseDto.setStatusCode(HttpStatus.FORBIDDEN.value());
            responseDto.setStatusMessage("댓글을 삭제할 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
        } catch (NoSuchElementException e) {
            responseDto.setStatusCode(HttpStatus.NOT_FOUND.value());
            responseDto.setStatusMessage("삭제할 댓글이 존재하지 않습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (Exception e) {
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("댓글 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    private final CommentLikeService commentLikeService;


    @PostMapping("/{commentId}/like")
    public ResponseEntity<ResponseDto<?>> likeComment(@PathVariable("commentId") Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ResponseDto<String> responseDto = new ResponseDto<>();
        try {
            commentLikeService.likeComment(commentId, userDetails.getUser().getUserId());
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Comment liked successfully");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Error liking comment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    // unlikeComment API 추가
    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<ResponseDto<?>> unlikeComment(@PathVariable("commentId") Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ResponseDto<String> responseDto = new ResponseDto<>();
        try {
            commentLikeService.unlikeComment(commentId, userDetails.getUser().getUserId());
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Comment unliked successfully");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Error unliking comment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    // 댓글 좋아요 수와 좋아요한 사용자 목록을 가져오는 API
    @GetMapping("/{id}/likes-user")
    public ResponseDto<LikeDataDto> getLikeCountAndLikedUsers(@PathVariable("id") Long commentId) {
        // commentLikeService를 통해 좋아요 수와 사용자 목록을 가져옴
        LikeDataDto likeData = commentLikeService.getLikeCountAndUsers(commentId);

        // ResponseDto 객체 생성 후 setItem 호출
        ResponseDto<LikeDataDto> response = new ResponseDto<>();
        response.setItem(likeData);

        return response; // 설정한 ResponseDto 반환

    }
}
