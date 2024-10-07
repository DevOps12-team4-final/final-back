package com.bit.finalproject.dto;


import com.bit.finalproject.entity.Workout;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDto {

    private Long workout_id;
    private String workoutName;
    private String mainCategory;
    private String subCategory;
    private String equipment;
    private String detailImage;

    public Workout toEntity() {
        return Workout.builder()
                .workout_id(this.workout_id)
                .workoutName(this.workoutName)
                .mainCategory(this.mainCategory)
                .subCategory(this.subCategory)
                .equipment(this.equipment)
                .detailImage(this.detailImage)
                .build();
    }
}
