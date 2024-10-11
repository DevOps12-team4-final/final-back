package com.bit.finalproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "followSeqGenerator",
        sequenceName = "FOLLOW_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "follow")
public class Follow {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "followSeqGenerator"
    )
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "followerId", referencedColumnName = "userId")
    private User follower;  // 팔로워 User 객체

    @ManyToOne
    @JoinColumn(name = "followingId", referencedColumnName = "userId")
    private User following;  // 팔로잉 User 객체

    // followDto에서 follow 엔티티로 변환하는 메서드 (toEntity)
    public Follow toEntity(User follower, User following) {
        return Follow.builder()
                .followId(this.followId)
                .follower(follower)        // 팔로워 User 객체 설정
                .following(following)      // 팔로잉 User 객체 설정
                .build();
    }

    // 팔로잉의 프로필 이미지를 가져오는 메서드
    public String getFollowingProfileImage() {
        return this.following != null ? this.following.getProfileImage() : null;
    }

    // toEntity 메서드에 프로필 이미지도 추가하는 예시
    public Follow toEntityWithProfileImage(User follower, User following) {
        String profileImage = following != null ? following.getProfileImage() : null;
        Follow followEntity = Follow.builder()
                .followId(this.followId)
                .follower(follower)        // 팔로워 User 객체 설정
                .following(following)      // 팔로잉 User 객체 설정
                .build();

        // 필요하다면 프로필 이미지 관련 로직을 처리
        if (profileImage != null) {
            // 프로필 이미지와 관련된 추가 로직을 여기에 작성
        }

        return followEntity;
    }
}

