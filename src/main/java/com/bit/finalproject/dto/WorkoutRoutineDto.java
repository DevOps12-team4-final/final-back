package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Workout;
import com.bit.finalproject.entity.WorkoutRoutine;
import com.bit.finalproject.entity.WorkoutSet;
<<<<<<< HEAD
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
=======
>>>>>>> origin/feature/workout-woobin
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutRoutineDto {
    private Long routineId;
    private int routineNumber;
    private Long planId;
    private Long workoutId;
    private String workoutName;
    private String mainCategory;
    private List<WorkoutSet> workoutSetList;

<<<<<<< HEAD

=======
>>>>>>> origin/feature/workout-woobin
    public WorkoutRoutine toEntity(Workout workout) {
        return WorkoutRoutine.builder()
                .routineId(this.routineId)
                .routineNumber(this.routineNumber)
                .workout(workout)
                .workoutSetList(workoutSetList)
                .build();
    }
}
