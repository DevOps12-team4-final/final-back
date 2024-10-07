package com.bit.finalproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;  // 게시글과의 관계

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;  // 좋아요한 사용자
}
