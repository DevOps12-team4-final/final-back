package com.bit.finalproject.controller;

import com.bit.finalproject.dto.FollowDto;
import com.bit.finalproject.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService FollowService;

    // 팔로우 API
    @PostMapping
    public ResponseEntity<Void> follow(@RequestBody FollowDto followDto) {
        FollowService.follow(followDto);
        return ResponseEntity.ok().build();
    }

    // 언팔로우 API
    @DeleteMapping
    public ResponseEntity<Void> unfollow(@RequestBody FollowDto followDto) {
        FollowService.unfollow(followDto);
        return ResponseEntity.ok().build();
    }
}
