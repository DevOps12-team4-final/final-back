package com.bit.finalproject.controller;

import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.dto.FeedHashtagDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.*;
import com.bit.finalproject.service.FeedLikeService;
import com.bit.finalproject.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
@Slf4j
public class FeedController {

    private final FeedService feedService;
    private final FeedLikeService feedLikeService;
    private final FollowRepository followRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final BookMarkRepository bookMarkRepository;

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

            log.info("post feedDto list : {}", postfeedDto);

            responseDto.setItem(postfeedDto);
            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");

            // 응답에는 생성된 리소스의 위치를 나타내는 URI()를 포함해 반환한다.
            return ResponseEntity.created(new URI("/feed")).body(responseDto);
        } catch (Exception e) {
            log.error("post error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());

            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PatchMapping("/modify/{feedId}")
    public ResponseEntity<?> modify(@PathVariable("feedId") Long feedId,
                                    @RequestPart("feedDto") FeedDto feedDto,
                                    @RequestPart(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,
                                    @RequestParam(name = "originFiles", required = false) String originFiles,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        ResponseDto<FeedDto> responseDto = new ResponseDto<>();

        try{
            log.info("update feedDto : {}", feedDto);
            FeedDto updateFeedDto = feedService.updateFeed(feedId, feedDto, uploadFiles, originFiles, customUserDetails.getUser());

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("updated");
            responseDto.setItem(updateFeedDto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            log.error("update error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());

            return ResponseEntity.internalServerError().body(responseDto);
        }
    }


    @GetMapping("/{feedId}")
    public ResponseEntity<?> getFeed(@PathVariable("feedId") Long feedId) {
        ResponseDto<FeedDto> responseDto = new ResponseDto<>();
        try{
            FeedDto feedDto = feedService.getFeed(feedId);
            responseDto.setItem(feedDto);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            return ResponseEntity.ok(responseDto);

        }catch (Exception e){
            log.error("get error: {}", e.getMessage());
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
            log.info("removeLike feedId : {}", feedId);

            feedLikeService.removeLike(feedId, userId);

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
    public ResponseEntity<?> getLikeCount(@PathVariable Long feedId,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        ResponseDto<Object> responseDto = new ResponseDto<>();

        try{
            log.info("getLikeCount feedId : {}", feedId);
            // 현재 사용자가 좋아요를 눌렀는지 여부 확인
            Long userId = customUserDetails.getUser().getUserId();

            // Feed 및 User 엔티티를 조회
            Feed feed = feedRepository.findById(feedId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid feed ID"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

            // 좋아요 여부 확인
            boolean isLiked = feedLikeRepository.existsByFeedAndUser(feed, user);

            // 좋아요 개수 가져오기
            int likeCount = feedLikeService.getLikeCount(feedId);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("likeCount", likeCount);
            responseBody.put("isLiked", isLiked);

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
                                                       @PageableDefault(page = 0, size = 24, sort = "regdate", direction = Sort.Direction.DESC) Pageable pageable) {

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
                // 좋아요 상태 확인을 위해 Feed와 User 엔티티 조회
                Feed feed = feedRepository.findById(feedDto.getFeedId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid feed ID"));
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
                boolean isLiked = feedLikeRepository.existsByFeedAndUser(feed, user);
                boolean isMarked = bookMarkRepository.existsByFeedAndUser(feed, user);
                feedDto.setLiked(isLiked);
                feedDto.setMarked(isMarked);
            });

            feedList.forEach(feedDto -> {
                // 해당 피드에 연결된 해시태그 리스트를 FeedHashtagDto로 변환
                List<FeedHashtagDto> feedHashtags = feedDto.getFeedHashtags();
                if (feedHashtags != null) {
                    feedDto.setFeedHashtags(feedHashtags); // FeedHashtagDto 리스트를 FeedDto에 설정
                }
            });

            // 성공 시 응답 데이터 설정
            responseDto.setStatusCode(HttpStatus.OK.value()); // 200
            responseDto.setStatusMessage("Feed list retrieved successfully.");
            responseDto.setItem(feedList);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            log.info(e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Failed to retrieve Feed list: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("/following")
    public ResponseEntity<?> getAllFollowingFeeds(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @PageableDefault(page = 0, size = 10, sort = "regdate", direction = Sort.Direction.DESC) Pageable pageable) {

        ResponseDto<Page<FeedDto>> responseDto = new ResponseDto<>();

        try{
            Long userId = customUserDetails.getUser().getUserId();
            Page<FeedDto> feedList = feedService.getAllFollowingFeeds(userId, pageable);

            // 팔로우 관계 확인
            feedList.forEach(feedDto -> {
                Long feedUserId = feedDto.getUserId();
                boolean isFollowing = followRepository.existsByFollower_UserIdAndFollowing_UserId(userId, feedUserId);
                feedDto.setFollowing(isFollowing);

                // 좋아요 상태 확인을 위해 Feed와 User 엔티티 조회
                Feed feed = feedRepository.findById(feedDto.getFeedId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid feed ID"));
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

                boolean isLiked = feedLikeRepository.existsByFeedAndUser(feed, user);
                boolean isMarked = bookMarkRepository.existsByFeedAndUser(feed, user);
                feedDto.setLiked(isLiked);
                feedDto.setMarked(isMarked);
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

    @GetMapping("/my-page")
    public ResponseEntity<?> getFeedsByUserId(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDto<List<FeedDto>> responseDto = new ResponseDto<>();
        try{
            Long userId = customUserDetails.getUser().getUserId();
            System.out.println(userId);
            List<FeedDto> feedList = feedService.getFeedsByUserId(userId);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Feed list retrieved successfully.");
            responseDto.setItem(feedList);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Failed to retrieve Feed list: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

}
