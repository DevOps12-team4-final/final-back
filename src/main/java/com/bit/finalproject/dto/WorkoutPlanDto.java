package com.bit.finalproject.dto;


import com.bit.finalproject.entity.User;
import com.bit.finalproject.entity.WorkoutPlan;
import com.bit.finalproject.entity.WorkoutRoutine;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlanDto {

    private Long planId;
    private Long userId;
    private LocalDateTime planDate;
    private LocalDateTime regdate;
    private LocalDateTime moddate;
    private LocalDateTime planTime;
    private boolean planCheck;
    private List<WorkoutRoutine> workoutRoutineList;

    public WorkoutPlan toEntity(User user) {
        return WorkoutPlan.builder()
                .planId(this.planId)
                .user(user)
                .planDate(this.planDate)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .planTime(this.planTime)
                .planCheck(this.planCheck)
                .build();
    }
}
