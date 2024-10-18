package com.bit.finalproject.entity;

import com.bit.finalproject.dto.UserDataDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_data")  // 테이블 이름을 소문자로 통일
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memberSeqGenerator")
    private Long dataId;  // camelCase로 변경

    @ManyToOne
    @JoinColumn(name = "memberId", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "detailId", referencedColumnName = "detailId")  // UserDetail 테이블의 ID
    private UserDetail userDetail;

    // UserData 엔티티를 DTO로 변환
    public UserDataDto toDto() {
        return UserDataDto.builder()
                .dataId(this.dataId)
                .userId(user != null ? user.getUserId() : null)  // null 체크 추가
                .nickname(user != null ? user.getNickname() : null)  // null 체크 추가
                .userStatus(user != null ? user.getUserStatus() : null)  // null 체크 추가
                .memberDetailId(userDetail != null ? userDetail.getDetailId() : null)  // camelCase 통일
                .memberId(userDetail != null && userDetail.getUser() != null ?
                        userDetail.getUser().getUserId() : null)  // null 체크 추가
                .active(user != null ? user.isActive() : null)
                .gender(userDetail != null ? userDetail.getGender() : null)  // null 체크 추가
                .birthDate(userDetail != null ? userDetail.getBirthDate() : null)  // camelCase 통일
                .usingTitle(userDetail != null ? userDetail.getUsingTitle() : null)  // camelCase 통일
                .statusMessage(userDetail != null ? userDetail.getStatusMessage() : null)  // camelCase 통일
                .favoriteExercise(userDetail != null ? userDetail.getFavoriteExercise() : null)  // 추가 필드
                .favoriteExercisePlen(userDetail != null ? userDetail.getFavoriteExercisePlen() : null)  // 추가 필드
                .totalWeightLifted(userDetail != null ? userDetail.getTotalWeightLifted() : 0) // 추가 필드
                .totalMountainsClimbed(userDetail != null ? userDetail.getTotalMountainsClimbed() : 0) // 추가 필드
                .consecutiveWorkoutDays(userDetail != null ? userDetail.getConsecutiveWorkoutDays() : 0) // 추가 필드
                .yogaSessionsCompleted(userDetail != null ? userDetail.getYogaSessionsCompleted() : 0) // 추가 필드
                .totalDistanceCovered(userDetail != null ? userDetail.getTotalDistanceCovered() : 0.0) // 추가 필드
                .build();
    }
}
