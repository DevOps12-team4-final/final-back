package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.UserDetailDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.*;
import com.bit.finalproject.jwt.JwtProvider;
import com.bit.finalproject.repository.DeletionRequestRepository;
import com.bit.finalproject.repository.FollowRepository;
import com.bit.finalproject.repository.UserDetailRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bit.finalproject.common.FileUtils;
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
    private final UserDetailRepository userDetailRepository;
    private final DeletionRequestRepository deletionRequestRepository; // 삭제 요청 레포지토리 추가
    private final FollowRepository followRepository;

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
        userDto.setUserStatus(UserStatus.ACTIVE);
        // member.toDto() -> Entity객체인 member를 Dto객체로 변환한다.
        // DB에 저장된 사용자 정보를 Dto객체로 전달하기 위함이다.
        UserDto loginUserDto = user.toDto();

        // 사용자 상태에 따른 로그인 여부
        if(!loginUserDto.getUserStatus().equals(UserStatus.ACTIVE)){
            throw new RuntimeException("user not active");
        }

        loginUserDto.setPassword("");
        loginUserDto.setLastLoginDate(LocalDateTime.now());

        if(!loginUserDto.isActive()){
            throw new RuntimeException("User Is Banded");
        }
        loginUserDto.setUserId(user.getUserId());
        loginUserDto.setEmail(user.getEmail());
        loginUserDto.setNickname(user.getNickname());
        loginUserDto.setUsername(user.getUsername());
        loginUserDto.setTel(user.getTel());
        loginUserDto.setUserStatus(user.getUserStatus());
        loginUserDto.setRole(user.getRole());
        loginUserDto.setProfileImage(user.getProfileImage());


        // JWT 토큰 발급
        loginUserDto.setToken(jwtProvider.createJwt(user));
        System.out.println(loginUserDto.getToken());
        System.out.println(loginUserDto);
        return loginUserDto;
    }

    @Override
    public UserDto join(UserDto userDto) {
        String defaultProfileImageUrl = "profileImage/default-profile.jpg";

        // 기본 권한, 활동 상태, 기본 프로필 이미지 설정
        userDto.setRole("ROLE_USER");
        userDto.setUserStatus(UserStatus.ACTIVE);

        // 사용자의 프로필 이미지가 없을 경우, 기본 이미지로 설정
        if (userDto.getProfileImage() == null || userDto.getProfileImage().isEmpty()) {
            userDto.setProfileImage(defaultProfileImageUrl);
        }

        // 입력한 password를 passwordEncoder를 이용해 암호화
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // User 엔티티 생성
        User newUser = userDto.toEntity();

        // UserDetail을 생성하고 기본값을 설정한 뒤 유저와 연관 설정
        UserDetail userDetail = UserDetail.builder()
                .user(newUser)
                .gender("Not Specified")
                .birthDate("0000-00-00")
                .usingTitle("User")
                .statusMessage("Hello, I'm using this app!")
                .followerCount(0)
                .followingCount(0)
                .build();

        // User에 UserDetail 연결
        newUser.setUserDetail(userDetail);

        // User와 UserDetail을 함께 저장
        UserDto joinedUserDto = userRepository.save(newUser).toDto();

        // 보안을 위해 password를 빈 문자열로 설정
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
    public UserDetailDto getmypage(Long userId) {
        // UserDetail을 조회하고, 존재하지 않을 경우 새로 생성하여 기본값 설정 후 저장
        UserDetail userDetail = userDetailRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    // User 엔티티 조회 (UserDetail 생성에 필요)
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    // 기본값이 설정된 새 UserDetail 생성
                    UserDetail newUserDetail = UserDetail.builder()
                            .user(user)
                            .gender("Not Specified")         // 기본값
                            .birthDate("0000-00-00")          // 기본값
                            .usingTitle("User")               // 기본값
                            .statusMessage("Hello, I'm using this app!") // 기본값
                            .followerCount(0)
                            .followingCount(0)
                            .build();

                    // 기본값을 포함한 UserDetail 저장
                    return userDetailRepository.save(newUserDetail);
                });

        // UserDetail을 DTO로 변환하여 반환
        UserDetailDto rememberDetailDto = userDetail.toDto();
        return rememberDetailDto;
    }


    @Override
    public UserDetailDto getprofilepage(Long userId) {
        // User를 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // UserDetail 조회, 없을 경우 새로 생성하여 기본값과 함께 저장
        UserDetail userDetail = userDetailRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    UserDetail newUserDetail = UserDetail.builder()
                            .user(user)
                            .gender("Not Specified")         // 기본값
                            .birthDate("0000-00-00")          // 기본값
                            .usingTitle("User")               // 기본값
                            .statusMessage("Hello, I'm using this app!") // 기본값
                            .followerCount(0)
                            .followingCount(0)
                            .build();

                    // 기본값이 포함된 UserDetail 저장
                    return userDetailRepository.save(newUserDetail);
                });

        // UserDetail을 DTO로 변환하여 반환
        UserDetailDto userDetailDto = userDetail.toDto();
        return userDetailDto;
    }

    @Override
    public void deleteMember(Long userId) {
        // 사용자 삭제 요청 생성
        DeletionRequest deletionRequest = DeletionRequest.builder()
                .userId(userId)
                .requestTime(LocalDateTime.now())
                .build();

        // 삭제 요청 저장
        deletionRequestRepository.save(deletionRequest);
    }


    @Override
    public int countFollowers(Long memberId) {
        return followRepository.countFollowers(memberId);
    }

    @Override
    public int countFollowing(Long memberId) {
        return followRepository.countFollowing(memberId);
    }


    // 사용자 목록 보기 (페이징)
    @Override
    public  Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // 사용자 검색 및 필터
    @Override
    public  Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.findByKeyword(keyword, pageable);
    }

    // 특정 역할을 가진 사용자 필터링
    @Override
    public  Page<User> getUsersByRole(String role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }


    @Override
    public UserDto banUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자 상태를 BAN 상태로 변경
        if (user.getUserStatus() != UserStatus.BANNED) {
            user.setUserStatus(UserStatus.BANNED);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User is already banned");
        }

        return user.toDto();
    }




    public Map<String, String> telCheck(String tel) {

        Map <String, String> telCheckMap = new HashMap<>();

        long telCheck = userRepository.countByTel(tel);

        if (telCheck == 0) {
            telCheckMap.put("telCheckMsg", "not exist tel");
        } else {
            User user = userRepository.findByTel(tel);
            if(user != null) {
                telCheckMap.put("telCheckMsg", "exist tel");
                telCheckMap.put("email", user.getEmail());
                telCheckMap.put("nickname", user.getNickname());
            }
        }
        return telCheckMap;
    }

    @Override
    public UserDto modifyPw(UserDto userDto) {

        User existingUser = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User updatedUser = userRepository.save(existingUser);

        UserDto modifyPw = updatedUser.toDto();
        modifyPw.setPassword("");

        return modifyPw;
    }

}
