package com.bit.finalproject.controller;

import com.bit.finalproject.dto.FeedCommentDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.entity.FeedComment;
import com.bit.finalproject.service.FeedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/feed-comment")
@RequiredArgsConstructor
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

            // 사용자의 Member 정보 가져오기
            Long userId = userDetails.getMember().getUserId();  // CustomUserDetails에서 Member 가져옴
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
}
