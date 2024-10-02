package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.MemberDtailDto;
import com.bit.finalproject.dto.MemberDto;
import com.bit.finalproject.entity.Member;
import com.bit.finalproject.entity.MemberDtail;
import com.bit.finalproject.entity.UserStatus;
import com.bit.finalproject.jwt.JwtProvider;
import com.bit.finalproject.repository.MemberDtailRepository;
import com.bit.finalproject.repository.MemberRepository;
import com.bit.finalproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberDtailRepository memberDtailRepository;

    @Override
    public MemberDto login(MemberDto memberDto) {

        // memberRepository.findByEmail(memberDto.getEmail()) -> DB에 입력한 Email과 일치하는 값이 있는지 확인한다.
        // 일치하는 값이 있다면 Email의 회원정보를 member객체에 저장한다.
        // 일치하는 값이 없다면 orEleseThrow()로 예외처리한다.
        Member member = memberRepository.findByEmail(memberDto.getEmail()).orElseThrow(
                () -> new RuntimeException("Email not exist")
        );

        // 사용자가 입력한 password값과 DB에 저장되어있는 암호화된 password값을 비교한다.
        // 일치하지 않으면 예외처리한다.
        if(!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())){
            throw new RuntimeException("wrong password");
        }

        // member.toDto() -> Entity객체인 member를 Dto객체로 변환한다.
        // DB에 저장된 사용자 정보를 Dto객체로 전달하기 위함이다.
        MemberDto loginMemberDto = member.toDto();

        // 사용자 상태에 따른 로그인 여부
        if(!loginMemberDto.getUserStatus().equals(UserStatus.ACTIVE)){
            throw new RuntimeException("user not active");
        }

        loginMemberDto.setPassword("");
        loginMemberDto.setLastLoginDate(LocalDateTime.now());

        // JWT 토큰 발급
        loginMemberDto.setToken(jwtProvider.createJwt(member));

        return loginMemberDto;
    }

    @Override
    public MemberDto join(MemberDto memberDto) {

        // 권한, 활동중, 기본프로필이미지 설정
        memberDto.setRole("ROLE_USER");
        memberDto.setUserStatus(UserStatus.ACTIVE);
        memberDto.setProfileImageUrl("/images/profile.png");

        // 사용자가입력한 password를 passwordEncoder를 이용해 암호화한다.
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        // 1. memberDto.toEntity() -> Dto 객체를 Entity 객체로 변환시켜 DB에 저장할 수 있는 상태로 만든다.
        // 2. save 메서드는 Entity 객체를 DB에 저장하고, 저장된 Entity 객체를 반환한다.
        // 3. memberRepository.save(memberDto.toEntity()).toDto() -> Entity 객체를 타입에맞게 Dto객체로 다시 변환한다.
        MemberDto joinedMemberDto = memberRepository.save(memberDto.toEntity()).toDto();

        // DB에 저장된 후 joinedMemberDto의 password를 빈 문자열로 설정한다.
        // 보안적인 이유로 password가 클라이언트측에 노출되는 것을 방지하기 위함이다.
        joinedMemberDto.setPassword("");

        return joinedMemberDto;
    }

    @Override
    public Map<String, String> emailCheck(String email) {

        System.out.println("3");
        Map<String, String> emailCheckMap = new HashMap<>();

        long emailCheck = memberRepository.countByEmail(email);

        if (emailCheck == 0) {
            emailCheckMap.put("emailCheckMsg", "available email");
        } else {
            emailCheckMap.put("emailCheckMsg", "invalid email");
        }

        System.out.println("4");
        return emailCheckMap;
    }

    @Override
    public Map<String, String> nicknameCheck(String nickname) {

        Map <String, String> nicknameCheckMap = new HashMap<>();

        long nicknameCheck = memberRepository.countByNickname(nickname);

        if (nicknameCheck == 0) {
            nicknameCheckMap.put("nicknameCheckMsg", "available nickname");
        } else {
            nicknameCheckMap.put("nicknameCheckMsg", "invalid nickname");
        }

        return nicknameCheckMap;
    }

    @Override
    public MemberDtailDto getmypage(Long UserId) {
        MemberDtail memberDtail=  memberDtailRepository.findById(UserId).orElseThrow();

        MemberDtailDto ReMemberDtailDto = memberDtail.toDto();




        return ReMemberDtailDto ;
    }

    @Override
    public MemberDtailDto getprofilepage(Long UserId) {
        MemberDtail memberDtail=  memberDtailRepository.findById(UserId).orElseThrow();

        MemberDtailDto ReMemberDtailDto = memberDtail.toDto();




        return ReMemberDtailDto ;
    }

}
