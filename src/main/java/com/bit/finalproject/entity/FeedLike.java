package com.bit.finalproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "feedLikeSeqGenerator",
        sequenceName = "FEEDLIKE_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedLike {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "feedLikeSeqGenerator")
    private Long like_id;

    @ManyToOne
    @JoinColumn(name = "feedId")
    private Feed feed;  // 게시글과의 관계

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;  // 좋아요한 사용자
}
