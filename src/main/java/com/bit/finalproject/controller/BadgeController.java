package com.bit.finalproject.controller;

import com.bit.finalproject.dto.BadgeAlarmDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.UserBadge;
import com.bit.finalproject.service.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/badges")
public class BadgeController {

    private final BadgeService badgeService;


    @PostMapping("/alarm")
    public ResponseEntity<?> occurBadgeAlarm(@RequestBody BadgeAlarmDto badgeAlarmDto) {
        ResponseDto<BadgeAlarmDto> responseDto = new ResponseDto<>();

        try{
            log.info("badgeAlarm BadgeAlarmDto: {}", badgeAlarmDto.toString());
            BadgeAlarmDto alarmDto = badgeService.occurBadgeAlarm(badgeAlarmDto);

            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");
            responseDto.setItem(badgeAlarmDto);

            return ResponseEntity.ok(responseDto);
        }catch(Exception e){
            log.error("badge alarm errror : {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }

    }
//    @GetMapping\
}
