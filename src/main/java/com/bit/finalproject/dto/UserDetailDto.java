package com.bit.finalproject.dto;

import com.bit.finalproject.entity.User;
import com.bit.finalproject.entity.UserDetail;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDetailDto {

    private Long detailId; // 필드 이름을 dtailId로 수정
    private Long memberId; // Member의 ID만을 가지고 있음
    private String gender;
    private String phoneNumber; // snake_case에서 camelCase로 변경
    private String birthDate; // snake_case에서 camelCase로 변경
    private String usingTitle; // typo 수정 (useing -> using)
    private String statusMessage; // snake_case에서 camelCase로 변경
    private String favoriteExercise; // camelCase로 수정
    private String favoriteExercisePlen; // camelCase로 수정
    private Long badge1;
    private Long badge2;
    private Long badge3;

    private int followerCount; // 추가: 팔로워 수
    private int followingCount; // 추가: 팔로잉 수

    // MemberDetailDto를 MemberDetail 엔티티로 변환하는 메서드
    public UserDetail toEntity() {
        // Member 엔티티 생성 (memberId만 가지고 있으므로 new로 생성)
        User user = User.builder()
                .userId(this.memberId)
                .build();

        return UserDetail.builder()
                .detailId(this.detailId) // 필드 이름 수정 (camelCase로 변경)
                .user(user)  // Member 엔티티 설정
                .gender(this.gender)
                .phoneNumber(this.phoneNumber) // 필드 이름 수정 (camelCase로 변경)
                .birthDate(this.birthDate) // 필드 이름 수정 (camelCase로 변경)
                .usingTitle(this.usingTitle) // 필드 이름 수정 (camelCase로 변경)
                .statusMessage(this.statusMessage) // 필드 이름 수정 (camelCase로 변경)
                .favoriteExercise(this.favoriteExercise) // 필드 이름 수정 (camelCase로 변경)
                .favoriteExercisePlen(this.favoriteExercisePlen) // 필드 이름 수정 (camelCase로 변경)
                .badge1(this.badge1)
                .badge2(this.badge2)
                .badge3(this.badge3)
                .followerCount(this.followerCount) // 팔로워 수 추가
                .followingCount(this.followingCount) // 팔로잉 수 추가
                .build();
    }
}
