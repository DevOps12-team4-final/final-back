package com.bit.finalproject.controller;

import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.service.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/badges")
public class BadgeController {

    private final BadgeService badgeService;

//    @PostMapping("/add")
//    public ResponseDto<?> addBadge(@RequestParam Badge badge)

}
