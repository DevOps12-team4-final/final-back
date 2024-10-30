package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.UserDetailDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.*;
import com.bit.finalproject.jwt.JwtProvider;
import com.bit.finalproject.repository.*;
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

import static com.bit.finalproject.entity.UserStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final DeletionRequestRepository deletionRequestRepository; // 삭제 요청 레포지토리 추가
    private final FollowRepository followRepository;
    private  final RoomRepository roomRepository;

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

        // member.toDto() -> Entity객체인 member를 Dto객체로 변환한다.
        // DB에 저장된 사용자 정보를 Dto객체로 전달하기 위함이다.
        UserDto loginUserDto = user.toDto();
        if(loginUserDto.getUserStatus().equals(UserStatus.BANNED)){
            throw new RuntimeException("User Is Banded");
        }

        if(loginUserDto.getUserStatus().equals(UserStatus.WITHDRAWN)){
            throw new RuntimeException("User Is WITHDRAWN");
        }
        loginUserDto.setUserStatus(ACTIVE);
        // 사용자 상태에 따른 로그인 여부
        if(!loginUserDto.getUserStatus().equals(ACTIVE)){
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
        String defaultProfileImageUrl = "profileImage/default-profile.jpg";

        // 권한, 활동중, 기본프로필이미지 설정
        userDto.setRole("ROLE_USER");
        userDto.setUserStatus(ACTIVE);

        // 사용자의 프로필 이미지가 없을 경우, 기본 이미지로 설정
        if (userDto.getProfileImage() == null || userDto.getProfileImage().isEmpty()) {
            userDto.setProfileImage(defaultProfileImageUrl);
        }

        // 사용자가입력한 password를 passwordEncoder를 이용해 암호화한다.
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // 1. userDto.toEntity() -> Dto 객체를 Entity 객체로 변환시켜 DB에 저장할 수 있는 상태로 만든다.
        // 2. save 메서드는 Entity 객체를 DB에 저장하고, 저장된 Entity 객체를 반환한다.
        // 3. userRepository.save(userDto.toEntity()).toDto() -> Entity 객체를 타입에맞게 Dto객체로 다시 변환한다.
        UserDto joinedUserDto = userRepository.save(userDto.toEntity()).toDto();

        // DB에 저장된 후 joinedUserDto의 password를 빈 문자열로 설정한다.
        // 보안적인 이유로 password가 클라이언트측에 노출되는 것을 방지하기 위함이다.
        joinedUserDto.setPassword("");

        //join시 room 만들기
        roomRepository.save(Room.builder()
                        .title(joinedUserDto.getNickname()) //default 닉네임
                        .description("%s님의 채팅방 입니다".formatted(joinedUserDto.getNickname())) //default 내용
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .profileImg(null)
                        .user(joinedUserDto.toEntity())
                .build());

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

        UserDetail userDetail=  userDetailRepository.findById(UserId).orElseThrow();

        UserDetailDto ReMemberDetailDto = userDetail.toDto();




        return ReMemberDetailDto;
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

    @Override
    public void logout(long userId) {
        // userId로 User 엔티티 조회
        // User 엔티티를 UserDto로 변환
        UserDto userDto =  userRepository.findById(userId).get().toDto();
        // 로그아웃 처리: 예를 들어, 로그인 상태 필드 업데이트
        userDto.setUserStatus(UserStatus.valueOf("INACTIVE"));  // `loggedIn` 필드는 예시입니다.
        userRepository.save(userDto.toEntity());
    }

}
