package com.bit.finalproject.controller;

import com.bit.finalproject.dto.MemberDtailDto;
import com.bit.finalproject.dto.MemberDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//@RestController는 내부적으로 @ResponseBody를 포함하고 있어
//메서드의 반환 값을 뷰가 아닌 HTTP 응답 본문(body)으로 직렬화하여 클라이언트로 반환합니다.
@RestController
//상수, null 값이 아닌 필드만 포함하는 생성자 자동으로 생성한다.
@RequiredArgsConstructor
//log라는 이름의 Logger 객체를 생성한다. (info, debug, warn, error 등 로그메시지 사용가능)
@Slf4j
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDto memberDto) {
        ResponseDto<MemberDto> responseDto = new ResponseDto<>();

        System.out.println(memberDto.getEmail() + memberDto.getPassword());

        try {
            log.info("login memberDto: {}", memberDto.toString());
            MemberDto loginMemberDto = memberService.login(memberDto);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(loginMemberDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("login error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberDto memberDto) {
        ResponseDto<MemberDto> responseDto = new ResponseDto<>();

        try {
            log.info("join memberDto: {}", memberDto.toString());
            MemberDto joinMemberDto = memberService.join(memberDto);

            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");
            responseDto.setItem(joinMemberDto);

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
    public ResponseEntity<?> emailCheck(@RequestBody MemberDto memberDto) {
        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>();

        try{
            log.info("email-check: {}", memberDto.getEmail());

            Map<String, String> emailCheckMap = memberService.emailCheck(memberDto.getEmail());
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
    public ResponseEntity<?> nicknameCheck(@RequestBody MemberDto memberDto) {
        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>();

        try{
            log.info("nickname-check: {}", memberDto.getNickname());

            Map<String, String> nicknameCheckMap = memberService.nicknameCheck(memberDto.getNickname());
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

    @GetMapping("/my_page")
    public ResponseEntity<?> getMyPage(Authentication authentication) {
        ResponseDto<MemberDtailDto> responseDto = new ResponseDto<>();

        try {
            // Authentication 객체에서 사용자 이메일(또는 username) 가져오기
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long memberId = userDetails.getMember().getUserId(); // 사용자의 ID 가져오기

            // 사용자 ID를 이용해 마이페이지 정보 조회
            MemberDtailDto memberDtailDto = memberService.getmypage(memberId);

            // 성공 응답 설정
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(memberDtailDto);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            // 에러 로그 출력 및 실패 응답 설정
            log.error("Page Loading error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping("/ProfilePage/{UserId}")
    public ResponseEntity<?> getProfilePage(@PathVariable("UserId") long UserId) {
        ResponseDto<MemberDtailDto> responseDto = new ResponseDto<>();

        try {
            // 전달된 UserId를 이용해 사용자 프로필 정보 조회
            MemberDtailDto memberDtailDto = memberService.getprofilepage(UserId);

            // 성공 응답 설정
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(memberDtailDto);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            // 에러 로그 출력 및 실패 응답 설정
            log.error("Profile Loading error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PatchMapping
    public ResponseEntity<?> modify(@RequestPart("memberDto") MemberDto memberDto,
                                    @RequestPart("memberDtailDto") MemberDtailDto memberDtailDto,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    Authentication authentication) {
        ResponseDto<MemberDto> responseDto = new ResponseDto<>();

        try {
            log.info("modify memberDto: {}", memberDto);
            log.info("modify memberDtailDto: {}", memberDtailDto);





            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");

            return ResponseEntity.ok(responseDto);
        } catch(Exception e) {
            log.error("modify error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }



    


}
