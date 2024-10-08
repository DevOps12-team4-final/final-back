package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Fallow;
import com.bit.finalproject.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FallowDto {
    private Long fallowId;        // 팔로우 ID
    private Long followerId;      // 팔로워 ID
    private Long fallowingId;     // 팔로잉 ID
    private String followerName;  // 팔로워 이름 (추가적인 정보)
    private String followingName; // 팔로잉 이름 (추가적인 정보)

    // FallowDto를 엔티티로 변환하는 메서드
    public Fallow toEntity(User fallower, User fallowing) {
        return Fallow.builder()
                .fallowId(this.fallowId)
                .fallower(fallower)        // 팔로워 Member 객체
                .fallowing(fallowing)      // 팔로잉 Member 객체
                .build();
    }
}