package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.UserDetailDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.entity.UserDetail;
import com.bit.finalproject.entity.UserStatus;
import com.bit.finalproject.jwt.JwtProvider;
import com.bit.finalproject.repository.UserDetailRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public UserDto modifymember(UserDto userDto) {
        User user = userDto.toEntity();
        return userRepository.save(user).toDto();

    }

    @Override
    public UserDetailDto modifymemberDetail(User user, UserDetailDto userDetailDto) {
        UserDetail memberDtail = userDetailDto.toEntity();
        return userDetailRepository.save(memberDtail).toDto();    }

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
        System.out.println(loginUserDto.getToken());
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
        UserDetail userDetail=  userDetailRepository.findById(UserId).orElseThrow();

        UserDetailDto RememberDetailDto = userDetail.toDto();




        return RememberDetailDto ;
    }

    @Override
    public UserDetailDto getprofilepage(Long UserId) {
        User user=  userRepository.findById(UserId).orElseThrow();
        UserDetail userDetail=  userDetailRepository.findById(UserId).orElseThrow();

        UserDetailDto ReMemberDetailDto = userDetail.toDto();

        UserDto RememberDto = user.toDto();


        return ReMemberDetailDto;
    }

    @Override
    public void deleteMember(Long userId) {

    }

    @Override
    public int countFollowers(Long memberId) {
        return 0;
    }

    @Override
    public int countFollowing(Long memberId) {
        return 0;
    }


}