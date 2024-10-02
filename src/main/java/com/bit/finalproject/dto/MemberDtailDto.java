package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Member;
import com.bit.finalproject.entity.MemberDtail;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDtailDto {
    private Long id;
    private Long member_id;
    private String gender;
    private String phone_number;
    private String birth_date;
    private String useing_title;


    public MemberDtail toEntity(Member member){
        return MemberDtail.builder()
                .id(this.id)
                .member(member)
                .gender(this.gender)
                .phone_number(this.phone_number)
                .birth_date(this.birth_date)
                .useing_title(this.useing_title)
                .build();

    }


}
