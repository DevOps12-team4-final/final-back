package com.bit.finalproject.controller;

import com.bit.finalproject.dto.UserDataDto;
import com.bit.finalproject.dto.UserDetailDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.entity.User;
import jakarta.servlet.http.HttpSession;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> logout(HttpSession session) {
        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>();

        try {
            long userId = (long) session.getAttribute("userId");  // 예: 세션에 저장된 사용자 ID
            userService.logout(userId);
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

    @GetMapping("/my_page")
    public ResponseEntity<?> getMyPage(HttpSession session) {
        ResponseDto<UserDetailDto> responseDto = new ResponseDto<>();

        try {
            // Authentication 객체에서 사용자 이메일(또는 username) 가져오기
            long userId = (long) session.getAttribute("userId");  // 예: 세션에 저장된 사용자 ID

            // 사용자 ID를 이용해 마이페이지 정보 조회
            UserDetailDto userDetailDto = userService.getmypage(userId);

            // 팔로워 및 팔로잉 수 조회
            int followerCount = userService.countFollowers(userId);  // 팔로워 수
            int followingCount = userService.countFollowing(userId); // 팔로잉 수

            // 팔로워 및 팔로잉 정보를 DTO에 추가
            userDetailDto.setFollowerCount(followerCount);
            userDetailDto.setFollowingCount(followingCount);

            // 성공 응답 설정
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(userDetailDto);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            // 에러 로그 출력 및 실패 응답 설정
            log.error("Page Loading error: {}", e.getMessage());
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
    @GetMapping("/ProfilePage/{UserId}")
    public ResponseEntity<?> getProfilePage(@PathVariable("UserId") long UserId) {
        ResponseDto<UserDetailDto> responseDto = new ResponseDto<>();

        try {

            // 전달된 UserId를 이용해 사용자 프로필 정보 조회
            UserDetailDto memberDataDto = userService.getprofilepage(UserId);


            // 팔로워 및 팔로잉 수 조회
            int followerCount = userService.countFollowers(UserId);  // 팔로워 수
            int followingCount = userService.countFollowing(UserId); // 팔로잉 수

            // 팔로워 및 팔로잉 정보를 DTO에 추가
            memberDataDto.setFollowerCount(followerCount);
            memberDataDto.setFollowingCount(followingCount);
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

    @PatchMapping
    public ResponseEntity<?> modify(
            @RequestPart("memberDto") UserDto userDto,  // 회원 기본 정보
            @RequestPart("memberDetailDto") UserDetailDto userDetailDto,  // 회원 상세 정보
            @AuthenticationPrincipal CustomUserDetails customUserDetails,  // 로그인된 사용자 정보
            Authentication authentication) {  // 인증 정보 제공

        ResponseDto<UserDataDto> responseDto = new ResponseDto<>();  // 응답 객체 초기화

        try {
            // 요청 받은 회원 정보 출력 (디버깅용 로그)
            log.info("modify memberDto: {}", userDto);
            log.info("modify memberDetailDto: {}", userDetailDto);

            // 회원 기본 정보 수정
            userService.modifymember(userDto);  // 서비스에서 회원 기본 정보 수정

            // 현재 로그인된 사용자 정보에서 Member 추출
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            // 회원 상세 정보 수정
            userService.modifymemberDetail(user, userDetailDto);  // 서비스에서 회원 상세 정보 수정

            // 수정된 회원 정보를 DTO로 변환하여 응답에 포함
            UserDataDto updatedUserDataDto = new UserDataDto(userDto, userDetailDto);
            updatedUserDataDto.setDataId(null); // 새로 생성된 경우 dataId는 null로 설정

            // 성공 로그 출력
            log.info("modify memberDto: {}", userDto);
            log.info("modify memberDetailDto: {}", userDetailDto);

            // 응답에 수정된 회원 정보를 포함
            responseDto.setItem(updatedUserDataDto);  // 수정된 기본 정보와 상세 정보를 포함
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
            User user = userDetails.getUser(); // Member 객체 가져오기

            // 회원 삭제 서비스 호출
            userService.deleteMember(user.getUserId()); // 사용자 ID를 사용하여 삭제

            // 성공 로그 출력
            log.info("User with ID {} has been successfully marked as deleted.", user.getUserId());

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
