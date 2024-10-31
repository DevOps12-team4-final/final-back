package com.bit.finalproject.entity;

import com.bit.finalproject.dto.UserDetailDto;
import jakarta.persistence.*;
import lombok.*;



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
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;  // 유저와 1:1 관계

    @Builder.Default
    private String gender = "Not Specified"; // 기본값 "Not Specified"

    @Builder.Default
    private String birthDate = "0000-00-00"; // 기본값 "0000-00-00"

    @Builder.Default
    private String usingTitle = "User"; // 기본값 "User"

    @Builder.Default
    private String statusMessage = "Hello, I'm using this app!"; // 기본값 "Hello, I'm using this app!"

    @Builder.Default
    private int followerCount = 0; // 팔로워 수 (기본값 0)
    @Builder.Default
    private int followingCount = 0; // 팔로잉 수 (기본값 0)

    // UserDetail 엔티티를 DTO로 변환하는 메서드
    public UserDetailDto toDto() {
        return UserDetailDto.builder()
                .detailId(this.detailId)
                .userId(this.user != null ? this.user.getUserId() : null)
                .gender(this.gender)
                .birthDate(this.birthDate)
                .statusMessage(this.statusMessage)
                .followerCount(this.followerCount)
                .followingCount(this.followingCount)
                .build();
    }
}
