package com.bit.finalproject.controller;


import com.bit.finalproject.dto.StatisticsDto;
import com.bit.finalproject.service.StatisticsService;
import com.bit.finalproject.service.impl.BadgeEvaluationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


}
