package com.bit.finalproject.controller;

import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.BookMarkRepository;
import com.bit.finalproject.repository.FeedRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookMarks")
@RequiredArgsConstructor
@Slf4j
public class BookMarkController {

    private final BookMarkService bookMarkService;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;


    @PostMapping("/{feedId}")
    public ResponseEntity<?> addBookMark(@PathVariable Long feedId,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUser().getUserId();
        ResponseDto<Object> responseDto = new ResponseDto<>();
        try{
            bookMarkService.addBookMark(feedId, userId);

            responseDto.setStatusCode(HttpStatus.OK.value());  // 200 OK
            responseDto.setStatusMessage("BookMark added successfully.");
            return ResponseEntity.ok(responseDto);
        } catch(Exception e){
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());  // 500 오류
            responseDto.setStatusMessage("Failed to add bookMark: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<?> removeBookMark(@PathVariable Long feedId,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getUser().getUserId();
        ResponseDto<Object> responseDto = new ResponseDto<>();
        try{
            bookMarkService.removeBookMark(feedId, userId);

            responseDto.setStatusCode(HttpStatus.OK.value());  // 200 OK
            responseDto.setStatusMessage("BookMark removed successfully.");
            return ResponseEntity.ok(responseDto);
        } catch(Exception e){
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());  // 500 오류
            responseDto.setStatusMessage("Failed to remove bookMark: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<?> getBookMarks(@PathVariable Long feedId,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUser().getUserId();
        ResponseDto<Object> responseDto = new ResponseDto<>();
        try{
            List<FeedDto> bookmarks = bookMarkService.getBookmarks(userId);

            // Feed 및 User 엔티티를 조회
            Feed feed = feedRepository.findById(feedId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid feed ID"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

            boolean isMarked = bookMarkRepository.existsByFeedAndUser(feed, user);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("isMarked", isMarked);
            responseBody.put("bookmarks", bookmarks);

            // 응답 설정
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Bookmarks retrieved successfully.");
            responseDto.setItem(responseBody);  // isMarked 및 bookmarks를 item에 설정

            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Failed to retrieve bookMarks: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

}
