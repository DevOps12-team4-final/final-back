package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Workout;
import com.bit.finalproject.entity.WorkoutRoutine;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutRoutineDto {
    private Long routine_id;
    private int routineNumber;

//    @OneToOne
//    @JoinColumn(name = "plan_id", referencedColumnName = "workoutId")
//    private WorkoutPlan workoutPlan;
//    private Long plan_id;

    @OneToOne
    @JoinColumn(name = "workout_id", referencedColumnName = "workout_id")
    private Long workout_id;

    public WorkoutRoutine toEntity(Workout workout) {
        return WorkoutRoutine.builder()
                .routine_id(this.routine_id)
                .routineNumber(this.routineNumber)
                .workout(workout)
                .build();
    }
}
