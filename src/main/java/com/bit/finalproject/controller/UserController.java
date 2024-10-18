package com.bit.finalproject.controller;

import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.service.CoolSmsService;
import com.bit.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.Coolsms;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//@RestController는 내부적으로 @ResponseBody를 포함하고 있어
//메서드의 반환 값을 뷰가 아닌 HTTP 응답 본문(body)으로 직렬화하여 클라이언트로 반환합니다.
@RestController
//상수, null 값이 아닌 필드만 포함하는 생성자 자동으로 생성한다.
@RequiredArgsConstructor
//log라는 이름의 Logger 객체를 생성한다. (info, debug, warn, error 등 로그메시지 사용가능)
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CoolSmsService coolSmsService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();

        System.out.println(userDto.getEmail() + userDto.getPassword());

        try {
            log.info("login userDto: {}", userDto);
            UserDto loginUserDto = userService.login(userDto);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(loginUserDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("login error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserDto userDto) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();

        try {
            log.info("join userDto: {}", userDto.toString());
            UserDto joinUserDto = userService.join(userDto);
            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");
            responseDto.setItem(joinUserDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("join error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>();

        try {
            log.info("logout success");
            Map<String, String> logoutMsgMap = new HashMap<>();
            SecurityContext securityContext = SecurityContextHolder.getContext();
            System.out.println(securityContext);
            securityContext.setAuthentication(null);
            SecurityContextHolder.setContext(securityContext);

            return ResponseEntity.ok(logoutMsgMap);
        } catch (Exception e) {
            log.error("logout error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    // Security 승인 옵션 잘확인하자
    // 이메일 체크
    @PostMapping("/email-check")
    public ResponseEntity<?> emailCheck(@RequestBody UserDto userDto) {
        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>();

        try{
            log.info("email-check: {}", userDto.getEmail());
            Map<String, String> emailCheckMap = userService.emailCheck(userDto.getEmail());
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(emailCheckMap);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            log.error("email-check error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    // 닉네임 체크
    @PostMapping("/nickname-check")
    public ResponseEntity<?> nicknameCheck(@RequestBody UserDto userDto) {
        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>();

        try{
            log.info("nickname-check: {}", userDto.getNickname());
            Map<String, String> nicknameCheckMap = userService.nicknameCheck(userDto.getNickname());
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(nicknameCheckMap);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            log.error("nickname-check error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    // 전화번호 체크
    @PostMapping("/tel-check")
    public ResponseEntity<?> telCheck(@RequestBody UserDto userDto) {
        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>();

        try{
            log.info("tel-check: {}", userDto.getTel());
            Map<String, String> telCheckMap = userService.telCheck(userDto.getTel());
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(telCheckMap);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            log.error("tel-check error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    // 인증메일 전송
    @PostMapping("/send")
    public ResponseEntity<?> sendSms(@RequestBody UserDto userDto) {
        ResponseDto<String> responseDto = new ResponseDto<>();

        try {
            log.info("send sms : {}", userDto.toString());
            String generatedCode = coolSmsService.sendSms(userDto.getTel());
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(generatedCode);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("send sms error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    // 비밀번호 수정
    @PostMapping("/modify-password")
    public ResponseEntity<?> modifyPassword(@RequestBody UserDto userDto) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();

        try{
            log.info("modify password: {}", userDto.toString());
            UserDto modifyPw = userService.modifyPw(userDto);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(modifyPw);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            log.error("modify password error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());

            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
}
