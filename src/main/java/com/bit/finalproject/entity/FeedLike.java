package com.bit.finalproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feed_Id")
    private Feed feed;  // 게시글과의 관계

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;  // 좋아요한 사용자
}
