package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Follow;

import com.bit.finalproject.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FollowDto {
    private Long followId;        // 팔로우 ID
    private Long followerId;      // 팔로워 ID
    private Long followingId;     // 팔로잉 ID
    private String followerName;  // 팔로워 이름 (추가적인 정보)
    private String followingName; // 팔로잉 이름 (추가적인 정보)
    private String followingNameprofileImage;
    // followDto를 엔티티로 변환하는 메서드
    public Follow toEntity(User follower, User following) {
        return Follow.builder()
                .followId(this.followId)
                .follower(follower)        // 팔로워 Member 객체
                .following(following)      // 팔로잉 Member 객체
                .build();
    }
}
