package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Workout;
import com.bit.finalproject.entity.WorkoutSet;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutSetDto {

    private Long set_id;
    private int setNumber;
    private double weight;
    private int reps;
    private boolean check;
    private LocalDateTime reptime;
    private Long workout_id;

    public WorkoutSet toEntity(Workout workout) {
        return WorkoutSet.builder()
                .set_id(this.set_id)
                .setNumber(this.setNumber)
                .weight(this.weight)
                .reps(this.reps)
                .check(this.check)
                .reptime(this.reptime)
                .workout(workout)
                .build();

    }
}
