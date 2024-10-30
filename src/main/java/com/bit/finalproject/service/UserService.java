package com.bit.finalproject.service;

import com.bit.finalproject.dto.UserDto;

import java.util.Map;

public interface UserService {
    UserDto login(UserDto userDto);

    UserDto join(UserDto userDto);

    Map<String, String> emailCheck(String email);

    Map<String, String> nicknameCheck(String nickname);

    Map<String, String> telCheck(String tel);

    UserDto modifyPw(UserDto userDto);
}
