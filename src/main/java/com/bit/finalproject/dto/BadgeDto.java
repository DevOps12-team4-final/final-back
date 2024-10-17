package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BadgeDto {
    private Long badgeId;
    private String badgeName;
    private String badgeContent;
    private String badgeGrade;
    private String badgeImage;

    // 배지 조건 리스트 추가
    private List<BadgeConditionDto> badgeConditions;

    public Badge toEntity() {
        return Badge.builder()
                .badgeId(this.badgeId)
                .badgeName(this.badgeName)
                .badgeContent(this.badgeContent)
                .badgeGrade(this.badgeGrade)
                .badgeImage(this.badgeImage)
                .build();
    }
}

