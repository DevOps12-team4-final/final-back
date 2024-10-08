package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.UserDetailDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.*;
import com.bit.finalproject.jwt.JwtProvider;
import com.bit.finalproject.repository.*;
import com.bit.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserDataRepository userDataRepository;
    @Autowired
    private DeletionRequestRepository deletionRequestRepository;
    @Autowired
    private FallowRepository fallowRepository;
    @Override
    public UserDto modifymember(UserDto userDto) {
        // 기존에 존재하는 멤버인지 확인 (아이디로 조회)
        User existingUser = userRepository.findById(userDto.getUserId())
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // 업데이트할 정보를 설정
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(userDto.getPassword()); // 패스워드가 암호화된 상태라면 바로 설정
        existingUser.setUsername(userDto.getUsername());
        existingUser.setNickname(userDto.getNickname());
        existingUser.setLastLoginDate(userDto.getLastLoginDate());
        existingUser.setUserStatus(userDto.getUserStatus());
        existingUser.setProfileImage(userDto.getProfileImage());
        existingUser.setRole(userDto.getRole());

        // 업데이트된 정보 저장 후 DTO로 반환
        return userRepository.save(existingUser).toDto();
    }

    @Override
    public UserDetailDto modifymemberDetail(User user, UserDetailDto UserDetailDto) {
        // member의 MemberDtail 정보가 존재하는지 확인
        UserDetail existingUserDetail = userDetailRepository.findById(UserDetailDto.getDetailId())
                .orElseGet(() -> UserDetail.builder()
                        .user(user) // 새로운 MemberDtail 생성 시 Member 설정
                        .build());

        // 업데이트할 정보 설정
        existingUserDetail.setGender(UserDetailDto.getGender());
        existingUserDetail.setPhoneNumber(UserDetailDto.getPhoneNumber());
        existingUserDetail.setBirthDate(UserDetailDto.getBirthDate());
        existingUserDetail.setUsingTitle(UserDetailDto.getUsingTitle());
        existingUserDetail.setStatusMessage(UserDetailDto.getStatusMessage());

        // 업데이트된 정보 저장 후 DTO로 반환
        return userDetailRepository.save(existingUserDetail).toDto();
    }


    @Override
    public UserDto login(UserDto userDto) {

        // memberRepository.findByEmail(memberDto.getEmail()) -> DB에 입력한 Email과 일치하는 값이 있는지 확인한다.
        // 일치하는 값이 있다면 Email의 회원정보를 member객체에 저장한다.
        // 일치하는 값이 없다면 orEleseThrow()로 예외처리한다.
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(
                () -> new RuntimeException("email not exist")
        );

        // 사용자가 입력한 password값과 DB에 저장되어있는 암호화된 password값을 비교한다.
        // 일치하지 않으면 예외처리한다.
        if(!passwordEncoder.matches(userDto.getPassword(), user.getPassword())){
            throw new RuntimeException("wrong password");
        }

        // member.toDto() -> Entity객체인 member를 Dto객체로 변환한다.
        // DB에 저장된 사용자 정보를 Dto객체로 전달하기 위함이다.
        UserDto loginUserDto = user.toDto();

        // 사용자 상태에 따른 로그인 여부
        if(!loginUserDto.getUserStatus().equals(UserStatus.ACTIVE)){
            throw new RuntimeException("user not active");
        }

        loginUserDto.setPassword("");
        loginUserDto.setLastLoginDate(LocalDateTime.now());

        // JWT 토큰 발급
        loginUserDto.setToken(jwtProvider.createJwt(user));

        return loginUserDto;
    }

    @Override
    public UserDto join(UserDto userDto) {

        // 권한, 활동중, 기본프로필이미지 설정
        userDto.setRole("ROLE_USER");
        userDto.setUserStatus(UserStatus.ACTIVE);
        userDto.setProfileImage("/images/profile.png");

        // 사용자가입력한 password를 passwordEncoder를 이용해 암호화한다.
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // 1. memberDto.toEntity() -> Dto 객체를 Entity 객체로 변환시켜 DB에 저장할 수 있는 상태로 만든다.
        // 2. save 메서드는 Entity 객체를 DB에 저장하고, 저장된 Entity 객체를 반환한다.
        // 3. memberRepository.save(memberDto.toEntity()).toDto() -> Entity 객체를 타입에맞게 Dto객체로 다시 변환한다.
        UserDto joinedUserDto = userRepository.save(userDto.toEntity()).toDto();

        // DB에 저장된 후 joinedMemberDto의 password를 빈 문자열로 설정한다.
        // 보안적인 이유로 password가 클라이언트측에 노출되는 것을 방지하기 위함이다.
        joinedUserDto.setPassword("");

        return joinedUserDto;
    }

    @Override
    public Map<String, String> emailCheck(String email) {

        Map<String, String> emailCheckMap = new HashMap<>();

        long emailCheck = userRepository.countByEmail(email);

        if (emailCheck == 0) {
            emailCheckMap.put("emailCheckMsg", "available email");
        } else {
            emailCheckMap.put("emailCheckMsg", "invalid email");
        }

        return emailCheckMap;
    }

    @Override
    public Map<String, String> nicknameCheck(String nickname) {

        Map <String, String> nicknameCheckMap = new HashMap<>();

        long nicknameCheck = userRepository.countByNickname(nickname);

        if (nicknameCheck == 0) {
            nicknameCheckMap.put("nicknameCheckMsg", "available nickname");
        } else {
            nicknameCheckMap.put("nicknameCheckMsg", "invalid nickname");
        }

        return nicknameCheckMap;
    }

    @Override
    public UserDetailDto getmypage(Long UserId) {
        UserDetail userDtail =  userDetailRepository.findById(UserId).orElseThrow();

        UserDetailDto reUserDetailDto = userDtail.toDto();




        return reUserDetailDto;
    }

    @Override
    public UserDetailDto getprofilepage(Long userId) {
        // userId로 회원(Member) 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        // 회원 상세(MemberDtail) 정보는 Member 엔티티에 이미 포함되어 있으므로 가져옴
        UserDetail userDtail = user.getUserDetail();
        if (userDtail == null) {
            throw new RuntimeException("해당 회원 상세 정보를 찾을 수 없습니다.");
        }

        // 조회된 Member와 MemberDtail 정보를 각각 DTO로 변환
        UserDto reUserDto = user.toDto();
        UserDetailDto reUserDetailDto = userDtail.toDto();

        // MemberDtailDto 반환
        return reUserDetailDto;
    }


    @Override
    public void deleteMember(Long userId) {
        // 회원 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 삭제 요청을 저장
        DeletionRequest deletionRequest = DeletionRequest.builder()
                .userId(userId)
                .requestTime(LocalDateTime.now())
                .build();

        deletionRequestRepository.save(deletionRequest);

        // 삭제 플래그 설정
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        // 회원 정보 업데이트
        userRepository.save(user);
    }

    // 특정 회원의 팔로워 수를 체크하는 메서드
    public int countFollowers(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        // 팔로워 수 카운트
        return fallowRepository.countByFallowing(user);
    }

    // 특정 회원의 팔로잉 수를 체크하는 메서드
    public int countFollowing(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        // 팔로잉 수 카운트
        return fallowRepository.countByFallower(user);
    }


}
