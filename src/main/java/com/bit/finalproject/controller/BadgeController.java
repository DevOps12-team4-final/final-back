package com.bit.finalproject.controller;

import com.bit.finalproject.service.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
//@RestController는 내부적으로 @ResponseBody를 포함하고 있어
//메서드의 반환 값을 뷰가 아닌 HTTP 응답 본문(body)으로 직렬화하여 클라이언트로 반환합니다.
@RestController

//log라는 이름의 Logger 객체를 생성한다. (info, debug, warn, error 등 로그메시지 사용가능)
@Slf4j

@RequestMapping("/badges") // API 엔드포인트
public class BadgeController {

    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    // "받기" 버튼 클릭에 대한 요청 처리
    @PostMapping("/reward/{userId}")
    public String rewardBadge(@PathVariable Long userId) {
        badgeService.checkAndRewardBadge(userId);
        return "배지가 지급되었습니다.";
    }
}
