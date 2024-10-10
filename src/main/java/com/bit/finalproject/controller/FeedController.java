package com.bit.finalproject.controller;


import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.CustomUserDetails;

import com.bit.finalproject.service.FeedLikeService;
import com.bit.finalproject.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;


@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
@Slf4j
public class FeedController {

    private final FeedService feedService;
    private final FeedLikeService feedLikeService;

    // @RequestBody: JSON, XML과 같은 형식으로 요청 본문 전체를 자바 객체로 변환할 때 사용.
    // @RequestPart: 멀티파트 요청에서 특정 파트를 처리할 때 사용. 주로 파일과 JSON 데이터를 함께 전송할 때 사용.
    @PostMapping("/post")
    public ResponseEntity<?> post(
            // 요청의 멀티파트 데이터중 feedDto라는 이름의 부분을 FeedDto 객체로 변환하여 받는다.
            @RequestPart("feedDto") FeedDto feedDto,
            // 요청에 포함된 파일을 배열의 형태로 받는다.
            // required = false 설정, 반드시 파일이 포함되지 않아도 된다.
            @RequestPart(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,
            // CustomUserDetails객체는 현재 로그인된 사용자 정보를 담고있다.
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            // 기본적으로 첫 번째 페이지(page = 0)에서 10개의 항목(size = 10)을 요청한다.
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        ResponseDto<FeedDto> responseDto = new ResponseDto<>();

        try {
            log.info("post feedDto : {}", feedDto);
            Page<FeedDto> feedDtoList = feedService.post(feedDto, uploadFiles, customUserDetails.getUser(), pageable);

            log.info("post feedDto list : {}", feedDtoList);

            responseDto.setPageItems(feedDtoList);
            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");

            // 응답에는 생성된 리소스의 위치를 나타내는 URI()를 포함해 반환한다.
            return ResponseEntity.created(new URI("/feeds")).body(responseDto);
        } catch (Exception e) {
            log.error("post error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());

            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    // 좋아요 추가
    @PostMapping("/{feedId}/like")
    public ResponseEntity<?> addLike(@PathVariable Long feedId,
                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long userId = customUserDetails.getUser().getUserId();

        ResponseDto<Object> responseDto = new ResponseDto<>();

        try {
            log.info("addLike feedId : {}", feedId);

            // 좋아요 추가 로직 실행
            feedLikeService.addLike(feedId, userId);

            // 성공 시 응답 코드 및 메시지 설정
            responseDto.setStatusCode(HttpStatus.OK.value());  // 200 OK
            responseDto.setStatusMessage("Like added successfully.");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            // 예외 발생 시 처리
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());  // 500 오류
            responseDto.setStatusMessage("Failed to add like: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    // 좋아요 취소
    @DeleteMapping("/{feedId}/like")
    public ResponseEntity<?> removeLike(@PathVariable Long feedId,
                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long userId = customUserDetails.getUser().getUserId();

        ResponseDto<Object> responseDto = new ResponseDto<>();

        try {
            log.info("addLike feedId : {}", feedId);

            // 좋아요 추가 로직 실행
            feedLikeService.removeLike(feedId, userId);

            // 성공 시 응답 코드 및 메시지 설정
            responseDto.setStatusCode(HttpStatus.OK.value());  // 200 OK
            responseDto.setStatusMessage("Like removed successfully.");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            // 예외 발생 시 처리
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());  // 500 오류
            responseDto.setStatusMessage("Failed to remove like: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    // 좋아요 총개수
    @GetMapping("/{feedId}/like-count")
    public ResponseEntity<?> getLikeCount(@PathVariable Long feedId) {

        ResponseDto<Object> responseDto = new ResponseDto<>();

        try{
            // 좋아요 개수 가져오기
            int likeCount = feedLikeService.getLikeCount(feedId);

            // 성공 시 응답 코드 및 좋아요 개수 설정
            responseDto.setStatusCode(HttpStatus.OK.value()); // 200 ok
            responseDto.setStatusMessage("Like count retrieved succeddfully.");
            responseDto.setItem(likeCount);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());  // 500 오류
            responseDto.setStatusMessage("Failed to retrieve like count: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }










    // 게시물 상세 정보 (게시물, 댓글, 좋아요 정보 포함)
//    @GetMapping("/{feedId}")
//    public ResponseEntity<FeedDto> getfeedDetails(@PathVariable Long feedId) {
//        // 게시물 정보
//        FeedDto feed = feedService.getfeedById(feedId);
//
//        // 댓글 정보
//        List<CommentDto> comments = commentService.getCommentsByFeedId(feedId);
//
//        // 좋아요 정보
//        int likeCount = likeService.getLikeCountByFeedId(feedId);
//
//        // FeedDto에 댓글과 좋아요 추가
//        feed.setComments(comments);
//        feed.setLikeCount(likeCount);
//
//        return ResponseEntity.ok(feed);
//    }


}
