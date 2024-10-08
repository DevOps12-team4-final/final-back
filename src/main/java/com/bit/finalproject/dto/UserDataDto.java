package com.bit.finalproject.dto;

import com.bit.finalproject.entity.User;
import com.bit.finalproject.entity.UserData;
import com.bit.finalproject.entity.UserDetail;
import com.bit.finalproject.entity.UserStatus;
import lombok.*;

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

    private Long memberDetailId;      // camelCase로 변경
    private Long memberId;            // camelCase로 변경
    private String gender;
    private String phoneNumber;        // camelCase로 변경
    private String birthDate;          // camelCase로 변경
    private String usingTitle;         // camelCase로 변경
    private String statusMessage;      // camelCase로 변경
    private String favoriteExercise;
    private String favoriteExercisePlen;
    private Long badge1;
    private Long badge2;
    private Long badge3;

    private int followerCount; // 추가: 팔로워 수
    private int followingCount; // 추가: 팔로잉 수

    // DTO에서 MemberDto와 MemberDetailDto를 받아서 초기화하는 생성자
    public UserDataDto(UserDto userDto, UserDetailDto userDetailDto) {
        this.dataId = null;  // ID는 null로 설정 (새로 생성될 경우)
        this.userId = userDto.getUserId();  // MemberDto에서 UserId 가져오기
        this.nickname = userDto.getNickname();
        this.userStatus = userDto.getUserStatus();
        this.profileImage = userDto.getProfileImage();
        this.token = userDto.getToken();  // 필요시 추가
        this.memberDetailId = userDetailDto.getDetailId();  // MemberDetailDto에서 ID 가져오기
        this.memberId = userDetailDto.getMemberId();  // camelCase로 변경
        this.gender = userDetailDto.getGender();
        this.phoneNumber = userDetailDto.getPhoneNumber();  // camelCase로 변경
        this.birthDate = userDetailDto.getBirthDate();  // camelCase로 변경
        this.usingTitle = userDetailDto.getUsingTitle();  // camelCase로 변경
        this.statusMessage = userDetailDto.getStatusMessage();  // camelCase로 변경
        this.favoriteExercise = userDetailDto.getFavoriteExercise();
        this.favoriteExercisePlen = userDetailDto.getFavoriteExercisePlen();
        this.badge1 = userDetailDto.getBadge1();
        this.badge2 = userDetailDto.getBadge2();
        this.badge3 = userDetailDto.getBadge3();
    }

    // 엔티티로 변환
    public UserData toEntity(User user, UserDetail userDetail) {
        return UserData.builder()
                .dataId(this.dataId)  // camelCase로 변경된 변수 사용
                .user(user)
                .userDetail(userDetail)
                .build();
    }
}
