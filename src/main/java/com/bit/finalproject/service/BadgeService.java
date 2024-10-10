package com.bit.finalproject.service;

import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.UserDetail;

public interface BadgeService {




    boolean checkCondition(UserDetail user, Badge badge);


    void checkAndRewardBadge(Long userId);
}
