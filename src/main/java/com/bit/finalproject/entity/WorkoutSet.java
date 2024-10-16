package com.bit.finalproject.entity;

import com.bit.finalproject.dto.WorkoutSetDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private boolean setChk; // check단어 mysql 명령어 중복으로 사용 불가능 //
    private LocalDateTime reptime;

    @ManyToOne
    @JoinColumn(name = "routine_id", referencedColumnName = "routineId")
    @JsonBackReference
    private WorkoutRoutine workoutRoutine;

    public WorkoutSetDto toDto() {
        return WorkoutSetDto.builder()
                .setId(this.setId)
                .setNumber(this.setNumber)
                .weight(this.weight)
                .reps(this.reps)
                .setChk(this.setChk)
                .reptime(this.reptime)
                .routineId(this.workoutRoutine.getRoutineId())
                .build();
    }
}
