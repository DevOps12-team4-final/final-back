package com.bit.finalproject.dto;

import com.bit.finalproject.entity.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDataDto {
    private Long dataId;             // camelCase로 추가
    private Long userId;             // camelCase로 변경
    private String nickname;
    private UserStatus userStatus;
    private String profileImage;
    private String token;
    private String tel;


    @Builder.Default
    private boolean active = true;

    private Long memberDetailId;      // camelCase로 변경
    private Long memberId;            // camelCase로 변경
    private String gender;
    private String birthDate;          // camelCase로 변경
    private String usingTitle;         // camelCase로 변경
    private String statusMessage;      // camelCase로 변경
    private String favoriteExercise;
    private String favoriteExercisePlen;


    private int totalWeightLifted;            // 운동 관련 기록 추가
    private int totalMountainsClimbed;        // 운동 관련 기록 추가
    private int consecutiveWorkoutDays;       // 운동 관련 기록 추가
    private int yogaSessionsCompleted;        // 운동 관련 기록 추가
    private double totalDistanceCovered;      // 운동 관련 기록 추가

    private int followerCount; // 추가: 팔로워 수
    private int followingCount; // 추가: 팔로잉 수

    // DTO에서 UserDto와 UserDetailDto를 받아서 초기화하는 생성자
    public UserDataDto(UserDto userDto, UserDetailDto userDetailDto) {
        this.dataId = null;  // ID는 null로 설정 (새로 생성될 경우)
        this.userId = userDto.getUserId();  // UserDto에서 userId 가져오기
        this.nickname = userDto.getNickname();
        this.userStatus = userDto.getUserStatus();
        this.profileImage = userDto.getProfileImage();
        this.token = userDto.getToken();  // 필요시 추가
        this.memberDetailId = userDetailDto.getDetailId();  // UserDetailDto에서 ID 가져오기
        this.memberId = userDetailDto.getMemberId();  // camelCase로 변경
        this.gender = userDetailDto.getGender();
        this.tel = userDto.getTel();  // camelCase로 변경
        this.birthDate = userDetailDto.getBirthDate();  // camelCase로 변경
        this.statusMessage = userDetailDto.getStatusMessage();  // camelCase로 변경


        this.followerCount = userDetailDto.getFollowerCount(); // 팔로워 수 초기화
        this.followingCount = userDetailDto.getFollowingCount(); // 팔로잉 수 초기화
    }

    // 엔티티로 변환
    public UserData toEntity(User userDto, UserDetail userDetail) {
        return UserData.builder()
                .dataId(this.dataId)  // camelCase로 변경된 변수 사용
                .user(userDto)
                .userDetail(userDetail)
                .build();
    }
}
