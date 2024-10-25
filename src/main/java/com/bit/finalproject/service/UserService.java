package com.bit.finalproject.service;

import com.bit.finalproject.dto.UserDetailDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserService {
     UserDto modifyUser(UserDto userDto) ;
     UserDetailDto modifyUserDetail(User user, UserDetailDto userDtailDto) ;
     UserDetailDto getmypage(Long UserId);
     UserDetailDto getprofilepage(Long UserId);
     void deleteUser(Long userId);
     int countFollowers(Long userId);
     int countFollowing(Long userId);

     UserDto banUser(Long id);

     Page<User> getAllUsers(Pageable pageable);

     Page<User> searchUsers(String keyword, Pageable pageable);

     Page<User> getUsersByRole(String role, Pageable pageable);
    UserDto login(UserDto userDto);

    UserDto join(UserDto userDto);

    Map<String, String> emailCheck(String email);

    Map<String, String> nicknameCheck(String nickname);

    Map<String, String> telCheck(String tel);

    UserDto modifyPw(UserDto userDto);
}
