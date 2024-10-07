package com.bit.finalproject.entity;

import com.bit.finalproject.dto.WorkoutDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "workoutSeqGenerator",
        sequenceName = "WORKOUT_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workout {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "workoutSeqGenerator"
    )
    private Long workout_id;
    private String workoutName;
    private String mainCategory;
    private String subCategory;
    private String equipment;
    private String detailImage;

    public WorkoutDto toDto() {
        return WorkoutDto.builder()
                .workout_id(this.workout_id)
                .workoutName(this.workoutName)
                .mainCategory(this.mainCategory)
                .subCategory(this.subCategory)
                .equipment(this.equipment)
                .detailImage(this.detailImage)
                .build();
    }
}
