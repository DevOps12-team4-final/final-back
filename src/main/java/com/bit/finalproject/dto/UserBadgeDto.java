package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.UserBadge;
import com.bit.finalproject.entity.UserDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadgeDto {
    private Long userBadgeId; // 유저 배지 ID
    private Long userDetailId; // 유저 상세 ID
    private Long badgeId; // 배지 ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 날짜 형식 지정
    private LocalDateTime regdate; // 배지 등록 날짜



public UserBadge toEntity(UserDetail userDetail, Badge badge) {
        return UserBadge.builder()
                .userBadgeId(this.userBadgeId)
                .userDetail(userDetail)
                .badge(badge)
                .regdate(this.regdate)
                .build();
    }
}
