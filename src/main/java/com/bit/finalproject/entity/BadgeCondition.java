package com.bit.finalproject.entity;

import com.bit.finalproject.dto.BadgeConditionDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "BadgeConditionSeqGenerator",
        sequenceName = "BADGE_CONDITION_SEQ",
        initialValue = 1,
        allocationSize = 1
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
            generator = "BadgeConditionSeqGenerator"
    )
    private Long badgeConditionId;

    @ManyToOne
    @JoinColumn(name = "badge_id", referencedColumnName = "badgeId")
    @JsonBackReference
    private Badge badge;

    private Long workoutId;
    private String badgeCondition;
    private Long conditionValue;
    private String hiddenCondition;

    public BadgeConditionDto toDto() {
        return BadgeConditionDto.builder()
                .badgeConditionId(this.badgeConditionId)
                .badgeId(badge.getBadgeId())
                .workoutId(this.workoutId)
                .badgeCondition(this.badgeCondition)
                .conditionValue(this.conditionValue)
                .hiddenCondition(this.hiddenCondition)
                .build();
    }
}
