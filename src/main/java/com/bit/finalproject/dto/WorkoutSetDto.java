package com.bit.finalproject.dto;

<<<<<<< HEAD
import com.bit.finalproject.entity.Workout;
=======
>>>>>>> origin/feature/workout-woobin
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
<<<<<<< HEAD
    private boolean check;
=======
    private boolean setChk;
>>>>>>> origin/feature/workout-woobin
    private LocalDateTime reptime;
    private Long routineId;

    public WorkoutSet toEntity(WorkoutRoutine workoutRoutine) {
        return WorkoutSet.builder()
                .setId(this.setId)
                .setNumber(this.setNumber)
                .weight(this.weight)
                .reps(this.reps)
<<<<<<< HEAD
                .check(this.check)
=======
                .setChk(this.setChk)
>>>>>>> origin/feature/workout-woobin
                .reptime(this.reptime)
                .workoutRoutine(workoutRoutine)
                .build();

    }
}
