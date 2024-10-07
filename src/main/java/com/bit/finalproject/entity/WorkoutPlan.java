package com.bit.finalproject.entity;

import com.bit.finalproject.dto.WorkoutPlanDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
        name = "workoutPlanSeqGenerator",
        sequenceName = "WORKOUTPLAN_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlan {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "workoutPlanSeqGenerator"
    )
    private Long plan_id;

    @OneToOne
    @JoinColumn( name = "member_id", referencedColumnName = "UserId")
    private Member member;

    private LocalDateTime plan_date;
    private LocalDateTime regdate;
    private LocalDateTime moddate;
    private LocalDateTime plan_time;
    private boolean planCheck;

    public WorkoutPlanDto toDto() {
        return WorkoutPlanDto.builder()
                .plan_id(this.plan_id)
                .member_id(this.member.getUserId())
                .plan_date(this.plan_date)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .plan_time(this.plan_date)
                .planCheck(this.planCheck)
                .build();
    }
}
