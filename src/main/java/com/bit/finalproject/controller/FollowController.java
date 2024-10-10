package com.bit.finalproject.controller;

import com.bit.finalproject.dto.FollowDto;
import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    // 팔로우 API
    @PostMapping("/follow/{UserId}")
    public ResponseEntity<Void> follow(@PathVariable("UserId") long UserId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long memberId = userDetails.getUser().getUserId(); // 사용자의 ID 가져오기



        followService.follow(memberId ,UserId);
        return ResponseEntity.ok().build();
    }

    // 언팔로우 API
    @DeleteMapping("/follow/{UserId}")
    public ResponseEntity<Void> unfollow(@PathVariable("UserId") long UserId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long memberId = userDetails.getUser().getUserId(); // 사용자의 ID 가져오기



        followService.follow(memberId ,UserId);
        return ResponseEntity.ok().build();
    }

    // 팔로워 목록 조회 API
    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<FollowDto>> getFollowers(@PathVariable Long userId) {
        List<FollowDto> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    // 팔로우 목록 조회 API
    @GetMapping("/followees/{userId}")
    public ResponseEntity<List<FollowDto>> getFollowees(@PathVariable Long userId) {
        List<FollowDto> followees = followService.getFollowings(userId);
        return ResponseEntity.ok(followees);
    }
}
