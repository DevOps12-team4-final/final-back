package com.bit.finalproject.entity;

import com.bit.finalproject.dto.WorkoutSetDto;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
        name = "workoutSetSeqGenerator",
        sequenceName = "WORKOUTSET_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutSet {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "workoutSetSeqGenerator"
    )
    private Long set_id;
    private int setNumber;
    private double weight;
    private int reps;
    private boolean check;
    private LocalDateTime reptime;

    @OneToOne
    @JoinColumn(name = "workout_id", referencedColumnName = "workout_id")
    private Workout workout;

    public WorkoutSetDto toDto() {
        return WorkoutSetDto.builder()
                .set_id(this.set_id)
                .setNumber(this.setNumber)
                .weight(this.weight)
                .reps(this.reps)
                .check(this.check)
                .reptime(this.reptime)
                .workout_id(this.workout.getWorkout_id())
                .build();
    }
}
