package com.bit.finalproject.controller;

import com.bit.finalproject.entity.UserDetail;
import com.bit.finalproject.service.BadgeEvaluationService;

import com.bit.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/badge")
@RequiredArgsConstructor
public class BadgeEvaluationController {

    private final BadgeEvaluationService badgeEvaluationService;
    private final UserService userService;

    // 특정 배지의 조건 평가 API
    @PostMapping("/{userId}/{badgeId}")
    public ResponseEntity<Boolean> evaluateBadge(@PathVariable Long userId, @PathVariable Long badgeId) {
        // 유저의 상세 정보를 조회
        UserDetail userDetail = userService.getmypage(userId).toEntity();

        // 특정 배지 조건 평가
        boolean result = badgeEvaluationService.evaluateBadge(userId, badgeId, userDetail);

        return ResponseEntity.ok(result); // 평가 결과 반환 (true/false)
    }
}
