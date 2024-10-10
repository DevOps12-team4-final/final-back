package com.bit.finalproject.dto;

import com.bit.finalproject.entity.BadgeAlarm;
import com.bit.finalproject.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeAlarmDto {

    private Long badgeAlarmId;
    private Long alarmedUserId;
    private String alarmContent;
    private String alarmCheck;
    private Long alarmingUserId;
    private LocalDateTime regdate;

    public BadgeAlarm toEntity(User alarmedUser, User alarmingUser){
        return BadgeAlarm.builder()
                .badgeAlarmId(this.badgeAlarmId)
                .alarmedUser(alarmedUser)
                .alarmContent(this.alarmContent)
                .alarmCheck(this.alarmCheck)
                .alarmingUser(alarmingUser)
                .regdate(this.regdate)
                .build();
    }
}
