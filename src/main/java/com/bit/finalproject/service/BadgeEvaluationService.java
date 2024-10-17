package com.bit.finalproject.service;

import com.bit.finalproject.dto.BadgeConditionDto;
import com.bit.finalproject.dto.BadgeDto;
import com.bit.finalproject.entity.BadgeCondition;
import com.bit.finalproject.entity.UserDetail;

public interface BadgeEvaluationService {
    boolean evaluateBadge(Long userId, Long badgeId, UserDetail userDetail);

    BadgeDto addBadge(BadgeDto badgeDto);


    // 개별 조건을 평가하는 메서드
    boolean evaluateCondition(UserDetail userDetail, BadgeCondition condition);
}
