package com.bit.finalproject.dto;

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
public class MemberDto {

    private Long UserId;
    private String email;
    private String password;
    private String username;
    private String nickname;
    private LocalDateTime lastLoginDate;
    private UserStatus userStatus;
    private String profileImageUrl;
    private String role;
    private String token;

    public Member toEntity(){
        return Member.builder()
                .UserId(this.UserId)
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .nickname(this.nickname)
                .lastLoginDate(this.lastLoginDate)
                .userStatus(this.userStatus)
                .profileImageUrl(this.profileImageUrl)
                .role(this.role)
                .build();
    }
}
