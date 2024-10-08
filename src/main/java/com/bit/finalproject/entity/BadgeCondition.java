package com.bit.finalproject.entity;

import com.bit.finalproject.dto.BadgeConditionDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name="BadgeCondtionSeqGenerator",
        sequenceName="BADGE_CONDITION_SEQ",
        initialValue=1,
        allocationSize=1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeCondition {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "BadgeCondtionSeqGenerator"
    )
    private Long badgeConditionId;

    @ManyToOne
    @JoinColumn(name = "badge_id", referencedColumnName = "badgeId")
    @JsonBackReference
    private Badge badge;
    private Long workoutId;
    private String condition;
    private Long conditionValue;
    private String hiddenCondition;

    public BadgeConditionDto toDto(){
        return BadgeConditionDto.builder()
                .badgeConditionId(this.badgeConditionId)
                .badgeId(badge.getBadgeId())
                .workoutId(this.workoutId)
                .condition(this.condition)
                .conditionValue(this.conditionValue)
                .hiddenCondition(this.hiddenCondition)
                .build();

    }
}
