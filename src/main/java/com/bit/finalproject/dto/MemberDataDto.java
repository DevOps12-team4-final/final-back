package com.bit.finalproject.dto;

import com.bit.finalproject.entity.MemberData;
import com.bit.finalproject.entity.MemberDtail;
import com.bit.finalproject.entity.UserStatus;
import com.bit.finalproject.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDataDto {
    private Long Data_id;
    private Long UserId;
    private String nickname;
//    private LocalDateTime lastLoginDate;
    private UserStatus userStatus;
    private String profileImage;
    private String token;

    private Long member_detail_id;
    private Long member_id;
    private String gender;
    private String phone_number;
    private String birth_date;
    private String useing_title;
    private String status_message;
    public MemberData toEntity(Member member , MemberDtail memberDtail ){
        return MemberData.builder()
                .Data_id(this.Data_id)
                .member(member)
                .memberDtail(memberDtail)
                .build();
    }
}
