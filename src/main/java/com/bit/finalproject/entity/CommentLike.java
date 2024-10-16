package com.bit.finalproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentLikeId; // CommentLike 엔티티의 기본 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentId", nullable = false)
    @JsonBackReference  // 역방향 참조 무시
    private FeedComment feedComment; //FeedComment와의 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
