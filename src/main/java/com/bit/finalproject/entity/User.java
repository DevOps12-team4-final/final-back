package com.bit.finalproject.entity;

import com.bit.finalproject.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
        name = "memberSeqGenerator",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "memberSeqGenerator"
    )
    private Long userId;  // 사용자 ID
    @Column(unique = true)
    private String email;
    private String password;
    private String username;
    @Column(unique = true)
    private String nickname;
    private LocalDateTime regdate;
    private LocalDateTime moddate;
//    @Column(name = "lastLoginDate")
    private LocalDateTime lastLoginDate;

    @Enumerated(EnumType.STRING)
//    @Column(name = "user_status")
    private UserStatus userStatus;

    @Column(name = "profileImage")
    private String profileImage;
    private String role;

    @Builder.Default
    private boolean deleted = false; // 삭제 플래그
    private LocalDateTime deletedAt; // 삭제 요청 시간

    // 양방향 매핑: Member가 MemberDetail을 소유
    @OneToOne(mappedBy = "userDto", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserDetail userDetail;

    // 양방향 매핑 편의 메서드
    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
        if (userDetail != null) {
            userDetail.setUser(this);  // MemberDetail의 Member 설정
        }
    }

    // Member 엔티티를 DTO로 변환하는 메서드
    public UserDto toDto() {
        return UserDto.builder()
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
                .memberDetail(this.userDetail != null ? this.userDetail.toDto() : null)
                .build();
    }
}
