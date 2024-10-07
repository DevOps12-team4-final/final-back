package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Member;
import com.bit.finalproject.entity.MemberData;
import com.bit.finalproject.entity.MemberDtail;
import com.bit.finalproject.entity.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDataDto {
    private Long dataId;             // camelCase로 추가
    private Long userId;             // camelCase로 변경
    private String nickname;
    private UserStatus userStatus;
    private String profileImage;
    private String token;

    private Long memberDetailId;      // camelCase로 변경
    private Long memberId;            // camelCase로 변경
    private String gender;
    private String phoneNumber;        // camelCase로 변경
    private String birthDate;          // camelCase로 변경
    private String usingTitle;         // camelCase로 변경
    private String statusMessage;      // camelCase로 변경

    // DTO에서 MemberDto와 MemberDtailDto를 받아서 초기화하는 생성자
    public MemberDataDto(MemberDto memberDto, MemberDtailDto memberDtailDto) {
        this.dataId = null;  // ID는 null로 설정 (새로 생성될 경우)
        this.userId = memberDto.getUserId();  // MemberDto에서 UserId 가져오기
        this.nickname = memberDto.getNickname();
        this.userStatus = memberDto.getUserStatus();
        this.profileImage = memberDto.getProfileImage();
        this.token = memberDto.getToken();  // 필요시 추가
        this.memberDetailId = memberDtailDto.getDtailId();  // MemberDtailDto에서 ID 가져오기
        this.memberId = memberDtailDto.getMemberId();  // camelCase로 변경
        this.gender = memberDtailDto.getGender();
        this.phoneNumber = memberDtailDto.getPhoneNumber();  // camelCase로 변경
        this.birthDate = memberDtailDto.getBirthDate();  // camelCase로 변경
        this.usingTitle = memberDtailDto.getUsingTitle();  // camelCase로 변경
        this.statusMessage = memberDtailDto.getStatusMessage();  // camelCase로 변경
    }

    // 엔티티로 변환
    public MemberData toEntity(Member member, MemberDtail memberDtail) {
        return MemberData.builder()
                .dataId(this.dataId)  // camelCase로 변경된 변수 사용
                .member(member)
                .memberDtail(memberDtail)
                .build();
    }
}
