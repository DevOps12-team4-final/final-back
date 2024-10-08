package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.entity.UserStatus;
import com.bit.finalproject.jwt.JwtProvider;
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

    @Override
    public UserDto login(UserDto userDto) {

        // userRepository.findByEmail(userDto.getEmail()) -> DB에 입력한 Email과 일치하는 값이 있는지 확인한다.
        // 일치하는 값이 있다면 Email의 회원정보를 user객체에 저장한다.
        // 일치하는 값이 없다면 orEleseThrow()로 예외처리한다.
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(
                () -> new RuntimeException("email not exist")
        );

        // 사용자가 입력한 password값과 DB에 저장되어있는 암호화된 password값을 비교한다.
        // 일치하지 않으면 예외처리한다.
        if(!passwordEncoder.matches(userDto.getPassword(), user.getPassword())){
            throw new RuntimeException("wrong password");
        }

        // user.toDto() -> Entity객체인 user를 Dto객체로 변환한다.
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

        // 1. userDto.toEntity() -> Dto 객체를 Entity 객체로 변환시켜 DB에 저장할 수 있는 상태로 만든다.
        // 2. save 메서드는 Entity 객체를 DB에 저장하고, 저장된 Entity 객체를 반환한다.
        // 3. userRepository.save(userDto.toEntity()).toDto() -> Entity 객체를 타입에맞게 Dto객체로 다시 변환한다.
        UserDto joinedUserDto = userRepository.save(userDto.toEntity()).toDto();

        // DB에 저장된 후 joinedUserDto의 password를 빈 문자열로 설정한다.
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

}
