package com.bit.finalproject.entity;

import com.bit.finalproject.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// JPA 엔티티임을 나타내는 어노테이션, DB테이블과 매핑시킨다.
@Entity
@SequenceGenerator(
        name = "userSeqGenerator",
        sequenceName = "USER_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 엔티티가 매핑될 DB테이블 이름을 지정하는 어노테이션이다, 만약에 생략되면 클래스이름으로 매핑된다.
// @Table(name = "user1")
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
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserDetail userDetail;

    // 양방향 매핑 편의 메서드
    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
        if (userDetail != null) {
            userDetail.setUser(this);  // MemberDetail의 Member 설정
        }
    }

    // 한 명의 회원이 여러 개의 좋아요를 가질 수 있는 관계 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedLike> likes;  // 사용자가 누른 좋아요 리스트

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CommentLike> commentLikes = new ArrayList<>();

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
