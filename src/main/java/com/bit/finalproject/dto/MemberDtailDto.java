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
    private Long dtail_id;
    private Long member_id;
    private String gender;
    private String phone_number;
    private String birth_date;
    private String useing_title;
    private String status_message;


    public MemberDtail toEntity(Member member){
        return MemberDtail.builder()
                .dtail_id(this.dtail_id)
                .member(member)
                .gender(this.gender)
                .phone_number(this.phone_number)
                .birth_date(this.birth_date)
                .useing_title(this.useing_title)
                .status_message(this.status_message)
                .build();

    }


}
