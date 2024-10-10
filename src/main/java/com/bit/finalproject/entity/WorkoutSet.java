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
    private Long setId;
    private int setNumber;
    private double weight;
    private int reps;
    private boolean check;
    private LocalDateTime reptime;

    @ManyToOne
    @JoinColumn(name = "routine_id", referencedColumnName = "routineId")
    private WorkoutRoutine workoutRoutine;

    public WorkoutSetDto toDto() {
        return WorkoutSetDto.builder()
                .setId(this.setId)
                .setNumber(this.setNumber)
                .weight(this.weight)
                .reps(this.reps)
                .check(this.check)
                .reptime(this.reptime)
                .routineId(this.workoutRoutine.getRoutineId())
                .build();
    }
}
