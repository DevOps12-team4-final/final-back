package com.bit.finalproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "followSeqGenerator",
        sequenceName = "follOW_SEQ",
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
    private User follower;  // 필드명 수정

    @ManyToOne
    @JoinColumn(name = "followingId", referencedColumnName = "userId")
    private User following;  // 필드명 수정

    // followDto에서 follow 엔티티로 변환하는 메서드 (toEntity)
    public Follow toEntity(User follower, User following) {
        return Follow.builder()
                .followId(this.followId)
                .follower(follower)        // 팔로워 Member 객체 설정
                .following(following)      // 팔로잉 Member 객체 설정
                .build();
    }
}
