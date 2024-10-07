package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Member;
import com.bit.finalproject.entity.MemberDtail;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDtailDto {

    private Long dtailId; // 필드 이름을 dtailId로 수정
    private Long memberId;  // Member의 ID만을 가지고 있음
    private String gender;
    private String phoneNumber; // snake_case에서 camelCase로 변경
    private String birthDate;   // snake_case에서 camelCase로 변경
    private String usingTitle;   // typo 수정 (useing -> using)
    private String statusMessage; // snake_case에서 camelCase로 변경

    // MemberDtailDto를 MemberDtail 엔티티로 변환하는 메서드
    public MemberDtail toEntity() {
        // Member 엔티티 생성 (memberId만 가지고 있으므로 new로 생성)
        Member member = Member.builder()
                .userId(this.memberId)
                .build();

        return MemberDtail.builder()
                .dtail_id(this.dtailId) // 필드 이름 수정
                .member(member)  // Member 엔티티 설정
                .gender(this.gender)
                .phoneNumber(this.phoneNumber) // 필드 이름 수정
                .birthDate(this.birthDate) // 필드 이름 수정
                .usingTitle(this.usingTitle) // 필드 이름 수정
                .statusMessage(this.statusMessage) // 필드 이름 수정
                .build();
    }
}
