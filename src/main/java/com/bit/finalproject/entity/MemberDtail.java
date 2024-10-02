package com.bit.finalproject.entity;

import com.bit.finalproject.dto.MemberDtailDto;
import com.bit.finalproject.dto.MemberDto;
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
    private Long id;
    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "UserId")
    private Member member;
    private String gender;
    private String phone_number;
    private String birth_date;
    private String useing_title;

    public MemberDtailDto toDto() {
        return MemberDtailDto.builder()
                    .id(this.id)
                    .member_id(this.member.getUserId())
                    .gender(this.gender)
                    .phone_number(this.phone_number)
                    .birth_date(this.birth_date)
                    .useing_title(this.useing_title)
                    .build();


    }

}