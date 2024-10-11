package com.bit.finalproject.service;

import com.bit.finalproject.entity.UserDetail;

public interface BadgeEvaluationService {
    boolean evaluateBadge(Long userId, Long badgeId, UserDetail userDetail);
}
