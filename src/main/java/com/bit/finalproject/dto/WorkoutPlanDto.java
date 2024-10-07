package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Member;
import com.bit.finalproject.entity.WorkoutPlan;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlanDto {

    private Long plan_id;
    private Long member_id;
    private LocalDateTime plan_date;
    private LocalDateTime regdate;
    private LocalDateTime moddate;
    private LocalDateTime plan_time;
    private boolean planCheck;

    public WorkoutPlan toEntity(Member member) {
        return WorkoutPlan.builder()
                .plan_id(this.plan_id)
                .member(member)
                .plan_date(this.plan_date)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .plan_time(this.plan_date)
                .planCheck(this.planCheck)
                .build();
    }
}
