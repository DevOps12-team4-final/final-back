package com.bit.finalproject.dto;

import com.bit.finalproject.entity.UserStatus;
import com.bit.finalproject.entity.Uesr;
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
public class UesrDto {

    private Long userId;
    private String email;
    private String password;
    private String username;
    private String nickname;
    private LocalDateTime lastLoginDate;
    private UserStatus userStatus;
    private String profileImage;
    private String role;
    private String token;

    public Uesr toEntity(){
        return Uesr.builder()
                .userId(this.userId)
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .nickname(this.nickname)
                .lastLoginDate(this.lastLoginDate)
                .userStatus(this.userStatus)
                .profileImage(this.profileImage)
                .role(this.role)
                .build();
    }
}
