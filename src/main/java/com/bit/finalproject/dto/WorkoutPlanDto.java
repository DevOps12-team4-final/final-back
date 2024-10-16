package com.bit.finalproject.dto;

<<<<<<< HEAD

=======
>>>>>>> origin/feature/workout-woobin
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
<<<<<<< HEAD
    private Long userId;
=======
    private Long user_id;
>>>>>>> origin/feature/workout-woobin
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
