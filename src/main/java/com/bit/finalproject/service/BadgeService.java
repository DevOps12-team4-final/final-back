package com.bit.finalproject.service;

import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.UserDetail;

public interface BadgeService {
    void checkConditionsForAllUsers();

    void checkBadgeConditionsForUser(UserDetail user);

    boolean checkCondition(UserDetail user, Badge badge);

    void awardBadgeToUser(UserDetail user, Badge badge);

    void checkAndRewardBadge(Long userId);
}
