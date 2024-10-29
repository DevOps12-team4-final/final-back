package com.bit.finalproject.controller;


import com.bit.finalproject.dto.StatisticsDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.BanHistory;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.service.BanHistoryService;
import com.bit.finalproject.service.StatisticsService;
import com.bit.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
//@RestController는 내부적으로 @ResponseBody를 포함하고 있어
//메서드의 반환 값을 뷰가 아닌 HTTP 응답 본문(body)으로 직렬화하여 클라이언트로 반환합니다.
@RestController

//log라는 이름의 Logger 객체를 생성한다. (info, debug, warn, error 등 로그메시지 사용가능)
@Slf4j
@RequiredArgsConstructor

@RequestMapping("/adminpage/")
public class AdminPageController {
    private final UserService userService;
    private final StatisticsService statisticsService;
    private final BanHistoryService banHistoryService;


    @GetMapping("/summary")
    public StatisticsDto getStatisticsSummary() {
        return statisticsService.getStatisticsSummary();
    }

    // 사용자 목록 보기 (페이징 지원)
    @GetMapping("/list")
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    // 활성화 사용자수 보기 (페이징 지원)
    @GetMapping("/activist")
    public long getActivistAllUsers() {
        return statisticsService.countActiveUsers();
    }

    // 사용자 검색
    @GetMapping("/search")
    public Page<User> searchUsers(@RequestParam String keyword, Pageable pageable) {
        return userService.searchUsers(keyword, pageable);
    }

    // 역할별 사용자 필터링
    @GetMapping("/filter")
    public Page<User> getUsersByRole(@RequestParam String role, Pageable pageable) {
        return userService.getUsersByRole(role, pageable);
    }

    @PutMapping("/ban/{id}")
    public UserDto banUser(@PathVariable Long id, @RequestParam Long banDays, String reason) {
        UserDto bannedUser = userService.banUser(id);

        // 밴 성공 시 밴 기록과 기간 저장
        banHistoryService.addBanHistory(bannedUser.getUserId(), banDays, reason);

        return bannedUser;
    }

    // 전체알림


    //게시글삭제


}
