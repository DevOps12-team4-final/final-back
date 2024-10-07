package com.bit.finalproject.service;

import com.bit.finalproject.dto.MemberDtailDto;
import com.bit.finalproject.dto.MemberDto;
import com.bit.finalproject.entity.Member;

import java.util.Map;

public interface MemberService {


     MemberDto modifymember(MemberDto memberDto) ;


     MemberDtailDto modifymemberDtail(Member member,MemberDtailDto memberDtailDto) ;


    MemberDto login(MemberDto memberDto);

    MemberDto join(MemberDto memberDto);

    Map<String, String> emailCheck(String email);

    Map<String, String> nicknameCheck(String nickname);

    MemberDtailDto getmypage(Long UserId);

    MemberDtailDto getprofilepage(Long UserId);


    void deleteMember(Long userId);
}
