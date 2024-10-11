package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.User;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

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

    public Badge toEntity(User user){
        return Badge.builder()
                .badgeId(this.badgeId)
                .badgeName(this.badgeName)
                .badgeContent(this.badgeContent)
                .badgeGrade(this.badgeGrade)
                .badgeImage(this.badgeImage)
                .build();
    }
}
