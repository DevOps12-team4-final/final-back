package com.bit.finalproject.entity;

import com.bit.finalproject.dto.WorkoutRoutineDto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private Long routineId;
    private int routineNumber;

    @ManyToOne
    @JoinColumn(name = "plan_id", referencedColumnName = "planId")
    @JsonBackReference // 양방향 맵핑에서 순환 참조 에러 방지
    private WorkoutPlan workoutPlan;

    @ManyToOne
    @JoinColumn(name = "workout_id", referencedColumnName = "workoutId")
    private Workout workout;

    @OneToMany(mappedBy = "workoutRoutine", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<WorkoutSet> workoutSetList;

    public WorkoutRoutineDto toDto() {
        return WorkoutRoutineDto.builder()
                .routineId(this.routineId)
                .routineNumber(this.routineNumber)
                .workoutId(this.workout.getWorkoutId())
                .workoutName(this.workout.getWorkoutName())
                .mainCategory(this.workout.getMainCategory())
                .workoutSetList(workoutSetList)
//                .workoutSetDtoList(
//                        workoutSetList != null && workoutSetList.size() > 0
//                                ? workoutSetList.stream().map(WorkoutSet::toDto).toList()
//                                : new ArrayList<>()
//                )
                .build();
    }

}
