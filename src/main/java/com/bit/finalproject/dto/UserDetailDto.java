package com.bit.finalproject.dto;

import com.bit.finalproject.entity.User;
import com.bit.finalproject.entity.UserDetail;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDetailDto {

    private Long detailId; // 필드 이름을 dtailId로 수정
    private Long userId; // Member의 ID만을 가지고 있음
    private String gender;
    private String birthDate; // snake_case에서 camelCase로 변경
    private String statusMessage; // snake_case에서 camelCase로 변경
    private int followerCount; // 추가: 팔로워 수
    private int followingCount; // 추가: 팔로잉 수

    // MemberDetailDto를 MemberDetail 엔티티로 변환하는 메서드
    public UserDetail toEntity() {
        // Member 엔티티 생성 (memberId만 가지고 있으므로 new로 생성)
        User user = User.builder()
                .userId(this.userId)
                .build();

        return UserDetail.builder()
                .detailId(this.detailId) // 필드 이름 수정 (camelCase로 변경)
                .user(user)  // Member 엔티티 설정
                .gender(this.gender)
                .birthDate(this.birthDate) // 필드 이름 수정 (camelCase로 변경)
                .statusMessage(this.statusMessage) // 필드 이름 수정 (camelCase로 변경)
                .followerCount(this.followerCount) // 팔로워 수 추가
                .followingCount(this.followingCount) // 팔로잉 수 추가
                .build();
    }
}
