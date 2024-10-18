package com.bit.finalproject.entity;

import com.bit.finalproject.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
            generator = "userSeqGenerator"
    )
    private Long userId;

    @Column(unique=true, nullable = false)
    private String email;
    private String password;
    private String username; // 이름
    @Column(unique=true, nullable = false)
    private String nickname; // 닉네임
    @Column(unique=true, nullable = false)
    private String tel;
    private LocalDateTime regdate; // 등록일
    private LocalDateTime moddate; // 수정일
    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate; // 마지막 로그인 날짜

    // 계정 상태 Enum사용
    // Enum을 문자열로 저장 (예: "ACTIVE", "INACTIVCE", "WITHDRAWN")
    @Enumerated(EnumType.STRING)
    @Column(name = "user_Status")
    private UserStatus userStatus;

    // 프로필 이미지 파일을 경로로 저장하는 방식
    @Column(name = "profile_image")
    private String profileImage;
    private String role;

    // 한 명의 회원이 여러 개의 좋아요를 가질 수 있는 관계 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedLike> likes;  // 사용자가 누른 좋아요 리스트

    public UserDto toDto() {
        return UserDto.builder()
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
                .build();
    }
}
