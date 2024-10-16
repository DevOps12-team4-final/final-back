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
    private Long workoutId;
    private String workoutName;
    private String mainCategory;
    private String subCategory;
    private String equipment;
    private String detailImage;
    @Transient
    private int checkForAddWorkout; // 1 == 완료 체크

    public WorkoutDto toDto() {
        return WorkoutDto.builder()
                .workoutId(this.workoutId)
                .workoutName(this.workoutName)
                .mainCategory(this.mainCategory)
                .subCategory(this.subCategory)
                .equipment(this.equipment)
                .detailImage(this.detailImage)
                .build();
    }
}
