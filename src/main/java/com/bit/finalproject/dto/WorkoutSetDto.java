package com.bit.finalproject.dto;

import com.bit.finalproject.entity.WorkoutRoutine;
import com.bit.finalproject.entity.WorkoutSet;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutSetDto {

    private Long setId;
    private int setNumber;
    private double weight;
    private int reps;
    private boolean setChk;
    private LocalDateTime reptime;
    private Long routineId;

    public WorkoutSet toEntity(WorkoutRoutine workoutRoutine) {
        return WorkoutSet.builder()
                .setId(this.setId)
                .setNumber(this.setNumber)
                .weight(this.weight)
                .reps(this.reps)
                .setChk(this.setChk)
                .reptime(this.reptime)
                .workoutRoutine(workoutRoutine)
                .build();

    }
}
