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
@Table(name = "MemberData")
public class MemberData {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "memberSeqGenerator"
    )
    private Long Data_id;

    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "UserId")
    private Member member;


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "member_detail_id", referencedColumnName = "dtail_id"),
            @JoinColumn(name = "dtail_id", referencedColumnName = "member_id")
    })
    private MemberDtail memberDtail;


    public MemberDataDto MemberData() {
        return MemberDataDto.builder()
                .Data_id(this.Data_id)
                .UserId(member.getUserId())
                .nickname(member.getNickname())
                .userStatus(member.getUserStatus())
                .member_detail_id(memberDtail.getDtail_id())
                .member_id(memberDtail.getMember().getUserId())
                .gender(memberDtail.getGender())
                .phone_number(memberDtail.getPhone_number())
                .birth_date(memberDtail.getBirth_date())
                .useing_title(memberDtail.getUseing_title())
                .status_message(memberDtail.getStatus_message())
                .build();
    }


}
