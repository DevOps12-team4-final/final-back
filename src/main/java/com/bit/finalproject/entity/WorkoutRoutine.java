package com.bit.finalproject.entity;

import com.bit.finalproject.dto.WorkoutRoutineDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "workoutRoutineSeqGenerator",
        sequenceName = "WORKOUTROUTINE_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutRoutine {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "workoutRoutineSeqGenerator"
    )
    private Long routine_id;
    private int routineNumber;

//    @OneToOne
//    @JoinColumn(name = "plan_id", referencedColumnName = "workoutId")
//    private WorkoutPlan workoutPlan;

    @OneToOne
    @JoinColumn(name = "workout_id", referencedColumnName = "workoutId")
    private Workout workout;

    public WorkoutRoutineDto toDto() {
        return WorkoutRoutineDto.builder()
                .routine_id(this.routine_id)
                .routineNumber(this.routineNumber)
                .workout_id(this.workout.getWorkout_id())
                .build();
    }

}
