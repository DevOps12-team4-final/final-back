package com.bit.finalproject.dto;


import com.bit.finalproject.entity.Workout;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDto {

    private Long workoutId;
    private String workoutName;
    private String mainCategory;
    private String subCategory;
    private String equipment;
    private String detailImage;
    private int favoriteWorkout;

    public Workout toEntity() {
        return Workout.builder()
                .workoutId(this.workoutId)
                .workoutName(this.workoutName)
                .mainCategory(this.mainCategory)
                .subCategory(this.subCategory)
                .equipment(this.equipment)
                .detailImage(this.detailImage)
                .favoriteWorkout(this.favoriteWorkout)
                .build();
    }
}
