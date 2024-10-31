package com.bit.finalproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockId;

    // 차단한 사용자
    private Long userId;
    // 차단된 사용자
    private Long blockedUserId;

    private LocalDateTime createdAt = LocalDateTime.now();
}
