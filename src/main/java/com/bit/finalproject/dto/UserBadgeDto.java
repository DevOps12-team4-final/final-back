package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.UserBadge;
import com.bit.finalproject.entity.UserDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserBadgeDto {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "UserBadgeSeqGenerator"
    )
    private Long userBadgeId;
    private Long dtail_id;
    private Long badgeId;

    private LocalDateTime regdate;

    public UserBadge toEntity(UserDetail userDetail, Badge badge) {
        return UserBadge.builder()
                .userBadgeId(this.userBadgeId)
                .userDetail(userDetail)
                .badge(badge)
                .regdate(this.regdate)
                .build();
    }
}