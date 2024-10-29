package com.bit.finalproject.dto;

import com.bit.finalproject.entity.UserStatus;
import com.bit.finalproject.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {

    private Long userId;
    private String email;
    private String password;
    private String username;
    private String nickname;
    private String tel;
    private LocalDateTime lastLoginDate;

    private UserStatus userStatus;
    private String profileImage;
    private String role;
    private String token;





    private LocalDateTime deletedAt; // 삭제 요청 시간
    private UserDetailDto memberDetail;  // MemberDetailDto 포함

    // MemberDto를 Member 엔티티로 변환하는 메서드
    public User toEntity() {
        return User.builder()
                .userId(this.userId)
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .nickname(this.nickname)
                .tel(this.tel)
                .lastLoginDate(this.lastLoginDate)
                .userStatus(this.userStatus)
                .profileImage(this.profileImage)
                .role(this.role)
                .deletedAt(this.deletedAt)
                // MemberDetailDto가 null이 아닌 경우에만 엔티티로 변환
                .userDetail(this.memberDetail != null ? this.memberDetail.toEntity() : null)
                .build();
    }
}
