package com.bit.finalproject.controller;


import com.bit.finalproject.dto.StatisticsDto;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.service.StatisticsService;
import com.bit.finalproject.service.impl.BadgeEvaluationServiceImpl;
import com.bit.finalproject.service.impl.UserServiceImpl;
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

    private final StatisticsService statisticsService;

    @GetMapping("/summary")
    public StatisticsDto getStatisticsSummary() {
        return statisticsService.getStatisticsSummary();
    }

    // 사용자 목록 보기 (페이징 지원)
    @GetMapping("/list")
    public Page<User> getAllUsers(Pageable pageable) {
        return UserServiceImpl.getAllUsers(pageable);
    }

    // 사용자 검색
    @GetMapping("/search")
    public Page<User> searchUsers(@RequestParam String keyword, Pageable pageable) {
        return UserServiceImpl.searchUsers(keyword, pageable);
    }

    // 역할별 사용자 필터링
    @GetMapping("/filter")
    public Page<User> getUsersByRole(@RequestParam String role, Pageable pageable) {
        return UserServiceImpl.getUsersByRole(role, pageable);
    }

    // 특정사용자벤

    //게시글삭제

    //배지추가

    //전체알림

    // 운동항목 추가


}
