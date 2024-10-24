package com.bit.finalproject.controller;

import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.CustomUserDetails;
<<<<<<< HEAD
import com.bit.finalproject.repository.FollowRepository;
=======

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.Hashtag;
>>>>>>> 35a1433166b1f1cd73cb567dd787c98e3a848c70
import com.bit.finalproject.service.FeedLikeService;
import com.bit.finalproject.service.FeedService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import java.net.URI;
<<<<<<< HEAD
import java.util.HashMap;
import java.util.List;
import java.util.Map;
=======
import java.util.List;
>>>>>>> 35a1433166b1f1cd73cb567dd787c98e3a848c70


@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
@Slf4j
public class FeedController {

    private final FeedService feedService;
    private final FeedLikeService feedLikeService;
    private final FollowRepository followRepository;

    // @RequestBody: JSON, XML과 같은 형식으로 요청 본문 전체를 자바 객체로 변환할 때 사용.
    // @RequestPart: 멀티파트 요청에서 특정 파트를 처리할 때 사용. 주로 파일과 JSON 데이터를 함께 전송할 때 사용.
    @PostMapping("/post")
    public ResponseEntity<?> post(
            // 요청의 멀티파트 데이터중 FeedDto라는 이름의 부분을 FeedDto 객체로 변환하여 받는다.
            @RequestPart("feedDto") FeedDto feedDto,
            // 요청에 포함된 파일을 배열의 형태로 받는다.
            // required = false 설정, 반드시 파일이 포함되지 않아도 된다.
            @RequestPart(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,
            // CustomUserDetails객체는 현재 로그인된 사용자 정보를 담고있다.
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        ResponseDto<FeedDto> responseDto = new ResponseDto<>();

        try {
            log.info("post feedDto : {}", feedDto);
            FeedDto postfeedDto = feedService.post(feedDto, uploadFiles, customUserDetails.getUser());
            // 서비스 계층에서 FeedDto 리스트를 페이지네이션된 형태로 받음
//            Page<FeedDto> feedDtoList = feedService.post(feedDto, uploadFiles, customUserDetails.getUser(), pageable);


            log.info("post feedDto list : {}", postfeedDto);

            responseDto.setItem(postfeedDto);
            // Page<FeedDto>를 그대로 설정
//            responseDto.setItems(feedDtoList.getContent());
            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");

<<<<<<< HEAD
            // 응답에는 생성된 리소스의 위치를 나타내는 URI()를 포함해 반환한다.
            return ResponseEntity.created(new URI("/feed")).body(responseDto);
=======
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
>>>>>>> 35a1433166b1f1cd73cb567dd787c98e3a848c70
        } catch (Exception e) {
            log.error("post error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
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
            log.info("removeLike feedId : {}", feedId);

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

            Map<String, Integer> responseBody = new HashMap<>();
            responseBody.put("likeCount", likeCount);

            // 성공 시 응답 코드 및 좋아요 개수 설정
            responseDto.setStatusCode(HttpStatus.OK.value()); // 200 ok
            responseDto.setStatusMessage("Like count retrieved successfully.");
            responseDto.setItem(responseBody);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());  // 500 오류
            responseDto.setStatusMessage("Failed to retrieve like count: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getAllFeedsExcludingUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                       @PageableDefault(page = 0, size = 12, sort = "regdate", direction = Sort.Direction.DESC) Pageable pageable) {

        ResponseDto<Page<FeedDto>> responseDto = new ResponseDto<>();

        try{
            // 인증된 사용자의 userId를 가져옴
            Long userId = customUserDetails.getUser().getUserId(); // CustomUserDetails에서 userId를 가져옴
            log.info("userId : {}", userId);

            // 서비스에서 게시글 리스트 가져오기
            Page<FeedDto> feedList = feedService.getAllFeedsExcludingUser(userId, pageable);

            // 팔로우 관계 확인
            feedList.forEach(feedDto -> {
                Long feedUserId = feedDto.getUserId();
                boolean isFollowing = followRepository.existsByFollower_UserIdAndFollowing_UserId(userId, feedUserId);
                feedDto.setFollowing(isFollowing);
            });

            // 성공 시 응답 데이터 설정
            responseDto.setStatusCode(HttpStatus.OK.value()); // 200
            responseDto.setStatusMessage("Feed list retrieved successfully.");
            responseDto.setItem(feedList);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Failed to retrieve Feed list: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

<<<<<<< HEAD
    @GetMapping("/following")
    public ResponseEntity<?> getAllFollowingFeeds(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @PageableDefault(page = 0, size = 10, sort = "regdate", direction = Sort.Direction.DESC) Pageable pageable) {

        ResponseDto<Page<FeedDto>> responseDto = new ResponseDto<>();
=======
    @GetMapping("/search")
    public ResponseEntity<?> searchFeeds(@RequestParam String hashtag) {
        try {
            List<Feed> feeds = feedService.searchFeedsByHashtag(hashtag);
            return ResponseEntity.ok(feeds);
        } catch (EntityNotFoundException e) {
            // 예외가 발생했을 때, 적절한 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 해시태그를 가진 게시글이 없습니다.");
        } catch (Exception e) {
            // 그 외 일반적인 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
        }
    }
>>>>>>> 35a1433166b1f1cd73cb567dd787c98e3a848c70

        try{
            Long userId = customUserDetails.getUser().getUserId();
            Page<FeedDto> feedList = feedService.getAllFollowingFeeds(userId, pageable);
            // 팔로우 관계 확인
            feedList.forEach(feedDto -> {
                Long feedUserId = feedDto.getUserId();
                boolean isFollowing = followRepository.existsByFollower_UserIdAndFollowing_UserId(userId, feedUserId);
                feedDto.setFollowing(isFollowing);
            });

            responseDto.setStatusCode(HttpStatus.OK.value());   // 200
            responseDto.setStatusMessage("Feed list retrieved successfully.");
            responseDto.setItem(feedList);

            return ResponseEntity.ok(responseDto);
        } catch(Exception e){
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Failed to retrieve Feed list: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }


}
