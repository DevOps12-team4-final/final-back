package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.MemberDtail;
import com.bit.finalproject.entity.UserBadge;
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

    public UserBadge toEntity(MemberDtail memberDtail, Badge badge) {
        return UserBadge.builder()
                .userBadgeId(this.userBadgeId)
                .memberDtail(memberDtail)
                .badge(badge)
                .regdate(this.regdate)
                .build();
    }
}
