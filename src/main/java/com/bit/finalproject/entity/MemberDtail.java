package com.bit.finalproject.entity;

import com.bit.finalproject.dto.MemberDtailDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "memberdtail")
public class MemberDtail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dtail_id;  // 수정: 필드 이름을 dtailId로 변경

    // 양방향 매핑: MemberDtail이 Member를 참조
    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "userId")  // userId로 변경
    private Member member;

    private String gender;
    private String phoneNumber;  // 수정: snake_case에서 camelCase로 변경
    private String birthDate;     // 수정: snake_case에서 camelCase로 변경
    private String usingTitle;     // 수정: typo 수정 (useing -> using)
    private String statusMessage;   // 수정: snake_case에서 camelCase로 변경

    // MemberDtail 엔티티를 DTO로 변환하는 메서드
    public MemberDtailDto toDto() {
        return MemberDtailDto.builder()
                .dtailId(this.dtail_id)  // 수정: 필드 이름을 dtailId로 변경
                .memberId(this.member != null ? this.member.getUserId() : null)  // null 체크 추가
                .gender(this.gender)
                .phoneNumber(this.phoneNumber)
                .birthDate(this.birthDate)
                .usingTitle(this.usingTitle)
                .statusMessage(this.statusMessage)
                .build();
    }
}
