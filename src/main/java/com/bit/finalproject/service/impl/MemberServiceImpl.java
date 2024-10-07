package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.MemberDtailDto;
import com.bit.finalproject.dto.MemberDto;
import com.bit.finalproject.entity.*;
import com.bit.finalproject.jwt.JwtProvider;
import com.bit.finalproject.repository.*;
import com.bit.finalproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final MemberDataRepository memberDataRepository;
    @Autowired
    private DeletionRequestRepository deletionRequestRepository;
    @Autowired
    private FallowRepository fallowRepository;
    @Override
    public MemberDto modifymember(MemberDto memberDto) {
        // 기존에 존재하는 멤버인지 확인 (아이디로 조회)
        Member existingMember = memberRepository.findById(memberDto.getUserId())
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // 업데이트할 정보를 설정
        existingMember.setEmail(memberDto.getEmail());
        existingMember.setPassword(memberDto.getPassword()); // 패스워드가 암호화된 상태라면 바로 설정
        existingMember.setUsername(memberDto.getUsername());
        existingMember.setNickname(memberDto.getNickname());
        existingMember.setLastLoginDate(memberDto.getLastLoginDate());
        existingMember.setUserStatus(memberDto.getUserStatus());
        existingMember.setProfileImage(memberDto.getProfileImage());
        existingMember.setRole(memberDto.getRole());

        // 업데이트된 정보 저장 후 DTO로 반환
        return memberRepository.save(existingMember).toDto();
    }

    @Override
    public MemberDtailDto modifymemberDtail(Member member, MemberDtailDto memberDtailDto) {
        // member의 MemberDtail 정보가 존재하는지 확인
        MemberDtail existingMemberDtail = memberDtailRepository.findById(memberDtailDto.getDtailId())
                .orElseGet(() -> MemberDtail.builder()
                        .member(member) // 새로운 MemberDtail 생성 시 Member 설정
                        .build());

        // 업데이트할 정보 설정
        existingMemberDtail.setGender(memberDtailDto.getGender());
        existingMemberDtail.setPhoneNumber(memberDtailDto.getPhoneNumber());
        existingMemberDtail.setBirthDate(memberDtailDto.getBirthDate());
        existingMemberDtail.setUsingTitle(memberDtailDto.getUsingTitle());
        existingMemberDtail.setStatusMessage(memberDtailDto.getStatusMessage());

        // 업데이트된 정보 저장 후 DTO로 반환
        return memberDtailRepository.save(existingMemberDtail).toDto();
    }


    @Override
    public MemberDto login(MemberDto memberDto) {

        // memberRepository.findByEmail(memberDto.getEmail()) -> DB에 입력한 Email과 일치하는 값이 있는지 확인한다.
        // 일치하는 값이 있다면 Email의 회원정보를 member객체에 저장한다.
        // 일치하는 값이 없다면 orEleseThrow()로 예외처리한다.
        Member member = memberRepository.findByEmail(memberDto.getEmail()).orElseThrow(
                () -> new RuntimeException("email not exist")
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
        memberDto.setProfileImage("/images/profile.png");

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

        Map<String, String> emailCheckMap = new HashMap<>();

        long emailCheck = memberRepository.countByEmail(email);

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
    public MemberDtailDto getprofilepage(Long userId) {
        // userId로 회원(Member) 정보 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        // 회원 상세(MemberDtail) 정보는 Member 엔티티에 이미 포함되어 있으므로 가져옴
        MemberDtail memberDtail = member.getMemberDtail();
        if (memberDtail == null) {
            throw new RuntimeException("해당 회원 상세 정보를 찾을 수 없습니다.");
        }

        // 조회된 Member와 MemberDtail 정보를 각각 DTO로 변환
        MemberDto reMemberDto = member.toDto();
        MemberDtailDto reMemberDtailDto = memberDtail.toDto();

        // MemberDtailDto 반환
        return reMemberDtailDto;
    }


    @Override
    public void deleteMember(Long userId) {
        // 회원 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 삭제 요청을 저장
        DeletionRequest deletionRequest = DeletionRequest.builder()
                .userId(userId)
                .requestTime(LocalDateTime.now())
                .build();

        deletionRequestRepository.save(deletionRequest);

        // 삭제 플래그 설정
        member.setDeleted(true);
        member.setDeletedAt(LocalDateTime.now());

        // 회원 정보 업데이트
        memberRepository.save(member);
    }

    // 특정 회원의 팔로워 수를 체크하는 메서드
    public int countFollowers(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        // 팔로워 수 카운트
        return fallowRepository.countByFallowing(member);
    }

    // 특정 회원의 팔로잉 수를 체크하는 메서드
    public int countFollowing(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        // 팔로잉 수 카운트
        return fallowRepository.countByFallower(member);
    }


}
