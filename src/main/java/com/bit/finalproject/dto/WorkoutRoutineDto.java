package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Workout;
import com.bit.finalproject.entity.WorkoutRoutine;
import com.bit.finalproject.entity.WorkoutSet;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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


    public WorkoutRoutine toEntity(Workout workout) {
        return WorkoutRoutine.builder()
                .routineId(this.routineId)
                .routineNumber(this.routineNumber)
                .workout(workout)
                .workoutSetList(workoutSetList)
                .build();
    }
}
