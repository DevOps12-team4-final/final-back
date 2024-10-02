package com.bit.finalproject.service;

import com.bit.finalproject.dto.MemberDtailDto;
import com.bit.finalproject.dto.MemberDto;

import java.util.Map;

public interface MemberService {
    MemberDto login(MemberDto memberDto);

    MemberDto join(MemberDto memberDto);

    Map<String, String> emailCheck(String email);

    Map<String, String> nicknameCheck(String nickname);

    MemberDtailDto getmypage(Long UserId);

    MemberDtailDto getprofilepage(Long UserId);
}
