package com.bit.finalproject.entity;

import com.bit.finalproject.dto.UserDetailDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_detail")
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;  // ID 필드

    @OneToOne
    @JoinColumn(name = "memberId", referencedColumnName = "userId")
    private User user;  // 유저와 1:1 관계

    private String gender;
    private String birthDate;
    private String usingTitle;
    private String statusMessage;

    @Builder.Default
    private int followerCount = 0; // 팔로워 수 (기본값 0)
    @Builder.Default
    private int followingCount = 0; // 팔로잉 수 (기본값 0)

    // UserDetail 엔티티를 DTO로 변환하는 메서드
    public UserDetailDto toDto() {
        return UserDetailDto.builder()
                .detailId(this.detailId)
                .memberId(this.user != null ? this.user.getUserId() : null)
                .gender(this.gender)
                .birthDate(this.birthDate)
                .statusMessage(this.statusMessage)
                .followerCount(this.followerCount)
                .followingCount(this.followingCount)
                .build();
    }
}
