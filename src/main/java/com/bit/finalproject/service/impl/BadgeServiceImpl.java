package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.BadgeAlarmDto;
import com.bit.finalproject.entity.BadgeAlarm;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.entity.UserBadge;
import com.bit.finalproject.repository.BadgeAlarmRepository;
import com.bit.finalproject.repository.BadgeRepository;
import com.bit.finalproject.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {

    private final BadgeAlarmRepository badgeAlarmRepository;
    @Override
    public BadgeAlarmDto occurBadgeAlarm(BadgeAlarmDto badgeAlarmDto) {
        badgeAlarmDto.setAlarmCheck("F");
        badgeAlarmDto.setRegdate(LocalDateTime.now());

        User user = new User();
        user.setUserId(badgeAlarmDto.getAlarmedUserId());
        UserBadge userBadge = new UserBadge();
        userBadge.setUserBadgeId(badgeAlarmDto.getAlarmedUserId());

        BadgeAlarm badgeAlarm = badgeAlarmDto.toEntity(user, userBadge);

        return badgeAlarmRepository.save(badgeAlarm).toDto();
    }
}
