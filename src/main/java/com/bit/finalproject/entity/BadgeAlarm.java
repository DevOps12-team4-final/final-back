package com.bit.finalproject.entity;

import com.bit.finalproject.dto.BadgeAlarmDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
        name = "BadgeAlarmSeqGenerator",
        sequenceName = "BADGE_ALARMS_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class BadgeAlarm {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "BadgeAlarmSeqGenerator"
    )
    private Long badgeAlarmId;
    @ManyToOne
    @JoinColumn(name="alarm_user_id", referencedColumnName = "userId")
    private User alarmedUser;
    private String alarmContent;
    private String alarmCheck;
    @OneToOne
    @JoinColumn(name="user_badge_id", referencedColumnName = "userBadgeId")
    private UserBadge userBadge;
    private LocalDateTime regdate;

    public BadgeAlarmDto toDto(){
        return BadgeAlarmDto.builder()
                .badgeAlarmId(this.badgeAlarmId)
                .alarmedUserId(alarmedUser.getUserId())
                .alarmContent(this.alarmContent)
                .alarmCheck(this.alarmCheck)
                .userBadgeId(userBadge.getUserBadgeId())
                .regdate(this.regdate)
                .build();
    }


}
