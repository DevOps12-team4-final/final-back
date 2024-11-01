package com.bit.finalproject.service;

import com.bit.finalproject.dto.UserDetailDto;
import com.bit.finalproject.dto.UserDto;

import java.util.Map;

public interface UserService {
    UserDto login(UserDto userDto);

    UserDto join(UserDto userDto);

    Map<String, String> emailCheck(String email);

    Map<String, String> nicknameCheck(String nickname);

    Map<String, String> telCheck(String tel);

    UserDto modifyPw(UserDto userDto);

    UserDetailDto getmypage(Long userId);

    int countFollowers(Long userId);

    int countFollowing(Long userId);
}
