package com.bit.finalproject.entity;

import com.bit.finalproject.dto.WorkoutPlanDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private Long planId;

    @OneToOne // plan은 plan계획일 별로 1개씩만 가능
    @JoinColumn( name = "user_id", referencedColumnName = "userId") // Member 파일 User로 바뀌는 거 보고 수정하기
    private User user;

    private LocalDateTime planDate; // plan계획일
    private LocalDateTime regdate;
    private LocalDateTime moddate;
    private LocalDateTime planTime; // plan수행시간
    private boolean planCheck;

    // 한 게시물이 여러개의 파일을 가지고 있는 관계
    @OneToMany (mappedBy = "workoutPlan", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<WorkoutRoutine> workoutRoutineList;

    public WorkoutPlanDto toDto() {
        return WorkoutPlanDto.builder()
                .planId(this.planId)
                .userId(this.user.getUserId())
                .planDate(this.planDate)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .planTime(this.planTime)
                .planCheck(this.planCheck)
                .workoutRoutineList(workoutRoutineList)
                .build();
    }
}
