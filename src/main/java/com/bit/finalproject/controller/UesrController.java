package com.bit.finalproject.controller;

import com.bit.finalproject.dto.UesrDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.service.UesrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//@RestController는 내부적으로 @ResponseBody를 포함하고 있어
//메서드의 반환 값을 뷰가 아닌 HTTP 응답 본문(body)으로 직렬화하여 클라이언트로 반환합니다.
@RestController
//상수, null 값이 아닌 필드만 포함하는 생성자 자동으로 생성한다.
@RequiredArgsConstructor
//log라는 이름의 Logger 객체를 생성한다. (info, debug, warn, error 등 로그메시지 사용가능)
@Slf4j
@RequestMapping("/users")
public class UesrController {

    private final UesrService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UesrDto userDto) {
        ResponseDto<UesrDto> responseDto = new ResponseDto<>();

        System.out.println(userDto.getEmail() + userDto.getPassword());

        try {
            log.info("login userDto: {}", userDto);
            UesrDto loginUesrDto = userService.login(userDto);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(loginUesrDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("login error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UesrDto userDto) {
        ResponseDto<UesrDto> responseDto = new ResponseDto<>();

        try {
            log.info("join userDto: {}", userDto.toString());
            UesrDto joinUesrDto = userService.join(userDto);

            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");
            responseDto.setItem(joinUesrDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("join error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

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

    @PostMapping("/email-check")
    public ResponseEntity<?> emailCheck(@RequestBody UesrDto userDto) {
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

    @PostMapping("/nickname-check")
    public ResponseEntity<?> nicknameCheck(@RequestBody UesrDto userDto) {
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
}
