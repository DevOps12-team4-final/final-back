package com.bit.finalproject.dto;

import com.bit.finalproject.entity.UserStatus;
import com.bit.finalproject.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDto {

    private Long userId;  // 소문자 userId로 수정
    private String email;
    private String password;
    private String username;
    private String nickname;
    private LocalDateTime lastLoginDate;
    private UserStatus userStatus;
    private String profileImage;
    private String role;
    private String token;

    @Builder.Default
    private boolean deleted = false; // 삭제 플래그
    private LocalDateTime deletedAt; // 삭제 요청 시간
    private MemberDtailDto memberDtail;  // MemberDtailDto 포함

    // MemberDto를 Member 엔티티로 변환하는 메서드
    public Member toEntity() {
        return Member.builder()
                .userId(this.userId)
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .nickname(this.nickname)
                .lastLoginDate(this.lastLoginDate)
                .userStatus(this.userStatus)
                .profileImage(this.profileImage)
                .role(this.role)
                .deleted(this.deleted)
                .deletedAt(this.deletedAt)
                // MemberDtailDto가 null이 아닌 경우에만 엔티티로 변환
                .memberDtail(this.memberDtail != null ? this.memberDtail.toEntity() : null)
                .build();
    }
}
