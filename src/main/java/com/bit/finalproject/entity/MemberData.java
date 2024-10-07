package com.bit.finalproject.entity;

import com.bit.finalproject.dto.MemberDataDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member_data")  // 테이블 이름을 소문자로 통일
public class MemberData {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "memberSeqGenerator"
    )
    private Long dataId; // camelCase로 변경

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "userId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "dtail_id", referencedColumnName = "dtail_id") // MemberDtail 테이블의 ID
    private MemberDtail memberDtail;


    // MemberData 엔티티를 DTO로 변환
    public MemberDataDto toDto() {
        return MemberDataDto.builder()
                .dataId(this.dataId)
                .userId(member != null ? member.getUserId() : null)  // null 체크 추가
                .nickname(member != null ? member.getNickname() : null) // null 체크 추가
                .userStatus(member != null ? member.getUserStatus() : null) // null 체크 추가
                .memberDetailId(memberDtail != null ? memberDtail.getDtail_id() : null) // camelCase 통일
                .memberId(memberDtail != null && memberDtail.getMember() != null ?
                        memberDtail.getMember().getUserId() : null)
                .gender(memberDtail != null ? memberDtail.getGender() : null)
                .phoneNumber(memberDtail != null ? memberDtail.getPhoneNumber() : null) // camelCase 통일
                .birthDate(memberDtail != null ? memberDtail.getBirthDate() : null) // camelCase 통일
                .usingTitle(memberDtail != null ? memberDtail.getUsingTitle() : null) // camelCase 통일
                .statusMessage(memberDtail != null ? memberDtail.getStatusMessage() : null) // camelCase 통일
                .build();
    }
}
