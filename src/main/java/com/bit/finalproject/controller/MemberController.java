package com.bit.finalproject.controller;

import com.bit.finalproject.dto.MemberDataDto;
import com.bit.finalproject.dto.MemberDtailDto;
import com.bit.finalproject.dto.MemberDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.entity.Member;
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

            // 팔로워 및 팔로잉 수 조회
            int followerCount = memberService.countFollowers(memberId);  // 팔로워 수
            int followingCount = memberService.countFollowing(memberId); // 팔로잉 수

            // 팔로워 및 팔로잉 정보를 DTO에 추가
            memberDtailDto.setFollowerCount(followerCount);
            memberDtailDto.setFollowingCount(followingCount);

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
            MemberDtailDto memberDataDto = memberService.getprofilepage(UserId);

            // 성공 응답 설정
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            ;
            responseDto.setItem(memberDataDto);
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
    public ResponseEntity<?> modify(
            @RequestPart("memberDto") MemberDto memberDto,  // 회원 기본 정보
            @RequestPart("memberDtailDto") MemberDtailDto memberDtailDto,  // 회원 상세 정보
            @AuthenticationPrincipal CustomUserDetails customUserDetails,  // 로그인된 사용자 정보
            Authentication authentication) {  // 인증 정보 제공

        ResponseDto<MemberDataDto> responseDto = new ResponseDto<>();  // 응답 객체 초기화

        try {
            // 요청 받은 회원 정보 출력 (디버깅용 로그)
            log.info("modify memberDto: {}", memberDto);
            log.info("modify memberDtailDto: {}", memberDtailDto);

            // 회원 기본 정보 수정
            memberService.modifymember(memberDto);  // 서비스에서 회원 기본 정보 수정

            // 현재 로그인된 사용자 정보에서 Member 추출
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Member member = userDetails.getMember();

            // 회원 상세 정보 수정
            memberService.modifymemberDtail(member, memberDtailDto);  // 서비스에서 회원 상세 정보 수정

            // 수정된 회원 정보를 DTO로 변환하여 응답에 포함
            MemberDataDto updatedMemberDataDto = new MemberDataDto(memberDto, memberDtailDto);
            updatedMemberDataDto.setDataId(null); // 새로 생성된 경우 dataId는 null로 설정

            // 성공 로그 출력
            log.info("modify memberDto: {ok}", memberDto);
            log.info("modify memberDtailDto: {ok}", memberDtailDto);

            // 응답에 수정된 회원 정보를 포함
            responseDto.setItem(updatedMemberDataDto);  // 수정된 기본 정보와 상세 정보를 포함
            responseDto.setStatusCode(HttpStatus.OK.value());  // 응답 코드 200 설정
            responseDto.setStatusMessage("회원 정보가 성공적으로 수정되었습니다.");

            return ResponseEntity.ok(responseDto);  // 200 OK 응답 반환

        } catch (Exception e) {
            // 예외 처리 및 에러 로그 출력
            log.error("modify error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());  // 응답 코드 500 설정
            responseDto.setStatusMessage("서버 오류로 인해 회원 정보 수정에 실패했습니다: " + e.getMessage());

            return ResponseEntity.internalServerError().body(responseDto);  // 500 에러 응답 반환
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(Authentication authentication) {
        ResponseDto<String> responseDto = new ResponseDto<>(); // 응답 객체 초기화

        try {
            // 현재 로그인된 사용자 정보 가져오기
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Member member = userDetails.getMember(); // Member 객체 가져오기

            // 회원 삭제 서비스 호출
            memberService.deleteMember(member.getUserId()); // 사용자 ID를 사용하여 삭제

            // 성공 로그 출력
            log.info("User with ID {} has been successfully marked as deleted.", member.getUserId());

            // 응답 설정
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("회원 정보가 성공적으로 삭제 요청되었습니다.");

            return ResponseEntity.ok(responseDto); // 200 OK 응답 반환

        } catch (Exception e) {
            // 예외 처리 및 에러 로그 출력
            log.error("Error deleting user: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // 응답 코드 500 설정
            responseDto.setStatusMessage("서버 오류로 인해 회원 삭제에 실패했습니다: " + e.getMessage());

            return ResponseEntity.internalServerError().body(responseDto); // 500 에러 응답 반환
        }
    }










}
