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
    private String phoneNumber;
    private String birthDate;
    private String usingTitle;
    private String statusMessage;
    private String favoriteExercise;
    private String favoriteExercisePlen;

    // 직접 표시할 배지 ID
    private Long badge1;
    private Long badge2;
    private Long badge3;

    // 모든 배지 목록
    @OneToMany(mappedBy = "userDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserBadge> badgeList;  // 유저가 소유한 모든 배지 리스트

    // 운동 관련 기록 필드 (적절한 기본값 설정)
    @Builder.Default
    private int totalWeightLifted = 0;         // 운동 무게 총합 (기본값 0)
    @Builder.Default
    private int totalMountainsClimbed = 0;     // 등산한 산의 개수 (기본값 0)
    @Builder.Default
    private int consecutiveWorkoutDays = 0;    // 연속 운동일수 (기본값 0)
    @Builder.Default
    private int yogaSessionsCompleted = 0;     // 요가 수행 회수 (기본값 0)
    @Builder.Default
    private double totalDistanceCovered = 0.0; // 운동한 거리 (기본값 0.0)

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
                .phoneNumber(this.phoneNumber)
                .birthDate(this.birthDate)
                .usingTitle(this.usingTitle)
                .statusMessage(this.statusMessage)
                .favoriteExercise(this.favoriteExercise)
                .favoriteExercisePlen(this.favoriteExercisePlen)
                .badge1(this.badge1) // 직접 표시할 배지 ID
                .badge2(this.badge2) // 직접 표시할 배지 ID
                .badge3(this.badge3) // 직접 표시할 배지 ID
                .badgeList(this.badgeList)
                .totalWeightLifted(this.totalWeightLifted)
                .totalMountainsClimbed(this.totalMountainsClimbed)
                .consecutiveWorkoutDays(this.consecutiveWorkoutDays)
                .yogaSessionsCompleted(this.yogaSessionsCompleted)
                .totalDistanceCovered(this.totalDistanceCovered)
                .followerCount(this.followerCount)
                .followingCount(this.followingCount)
                .build();
    }
}
