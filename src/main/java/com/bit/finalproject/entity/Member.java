package com.bit.finalproject.entity;

import com.bit.finalproject.dto.MemberDto;
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
@Table(name = "member")
public class Member {

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
    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Column(name = "profile_image")
    private String profileImage;
    private String role;

    @Builder.Default
    private boolean deleted = false; // 삭제 플래그
    private LocalDateTime deletedAt; // 삭제 요청 시간

    // 양방향 매핑: Member가 MemberDtail을 소유
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberDtail memberDtail;

    // 양방향 매핑 편의 메서드
    public void setMemberDtail(MemberDtail memberDtail) {
        this.memberDtail = memberDtail;
        if (memberDtail != null) {
            memberDtail.setMember(this);  // MemberDtail의 Member 설정
        }
    }

    // Member 엔티티를 DTO로 변환하는 메서드
    public MemberDto toDto() {
        return MemberDto.builder()
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
                .memberDtail(this.memberDtail != null ? this.memberDtail.toDto() : null)
                .build();
    }
}
