package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.BadgeCondition;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BadgeConditionDto {
    private Long badgeConditionId;
    private Long badgeId;
    private Long workoutId;
    private String condition;
    private Long conditionValue;
    private String hiddenCondition;

    public BadgeCondition toEntity(Badge badge){
        return BadgeCondition.builder()
                .badgeConditionId(this.badgeConditionId)
                .badge(badge)
                .workoutId(this.workoutId)
                .condition(this.condition)
                .conditionValue(this.conditionValue)
                .hiddenCondition(this.hiddenCondition)
                .build();

    }
}