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
@Table(name = "memberdtail")
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;  // 수정: 필드 이름을 dtailId로 변경

    @OneToOne
    @JoinColumn(name = "memberId", referencedColumnName = "userId")  // userId로 변경
    private User user;

    private String gender;
    private String phoneNumber;  // 수정: snake_case에서 camelCase로 변경
    private String birthDate;     // 수정: snake_case에서 camelCase로 변경
    private String usingTitle;     // 수정: typo 수정 (useing -> using)
    private String statusMessage;   // 수정: snake_case에서 camelCase로 변경
    private String favoriteExercise;
    private String favoriteExercisePlen;
    private Long badge1;
    private Long badge2;
    private Long badge3;

    private int followerCount; // 추가: 팔로워 수
    private int followingCount; // 추가: 팔로잉 수

    // MemberDetail 엔티티를 DTO로 변환하는 메서드
    public UserDetailDto toDto() {
        return UserDetailDto.builder()
                .detailId(this.detailId)  // 수정: 필드 이름을 dtailId로 변경
                .memberId(this.user != null ? this.user.getUserId() : null)  // null 체크 추가
                .gender(this.gender)
                .phoneNumber(this.phoneNumber)
                .birthDate(this.birthDate)
                .usingTitle(this.usingTitle)
                .statusMessage(this.statusMessage)
                .favoriteExercise(this.favoriteExercise)
                .favoriteExercisePlen(this.favoriteExercisePlen)
                .badge1(this.badge1)
                .badge2(this.badge2)
                .badge3(this.badge3)
                .followerCount(this.followerCount) // DTO로 팔로워 수 추가
                .followingCount(this.followingCount) // DTO로 팔로잉 수 추가
                .build();
    }
}
