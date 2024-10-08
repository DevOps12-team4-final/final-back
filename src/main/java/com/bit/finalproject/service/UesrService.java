package com.bit.finalproject.service;

import com.bit.finalproject.dto.UesrDto;

import java.util.Map;

public interface UesrService {
    UesrDto login(UesrDto userDto);

    UesrDto join(UesrDto userDto);

    Map<String, String> emailCheck(String email);

    Map<String, String> nicknameCheck(String nickname);
}
