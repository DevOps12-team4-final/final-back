package com.bit.finalproject.service.impl;

import com.bit.finalproject.entity.BadgeCondition;
import com.bit.finalproject.entity.UserDetail;

import com.bit.finalproject.repository.BadgeRepository;
import com.bit.finalproject.service.BadgeEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeEvaluationServiceImpl implements BadgeEvaluationService {

    private final BadgeRepository badgeConditionRepository;



    // 특정 배지의 조건을 유저의 기록과 비교하여 트루/펄스를 반환하는 메서드
    public boolean evaluateBadge(Long userId, Long badgeId, UserDetail userDetail) {
        // DB에서 해당 배지의 조건을 가져옴
        List<BadgeCondition> badgeConditions = badgeConditionRepository.findAllByBadgeId(badgeId);

        // 유저의 운동 기록과 배지 조건을 비교
        return badgeConditions.stream().allMatch(condition -> evaluateCondition(userDetail, condition));
    }

    // 개별 조건을 평가하는 메서드
    private boolean evaluateCondition(UserDetail userDetail, BadgeCondition condition) {
        switch (condition.getBadgeCondition()) {
            case "totalWeightLifted":
                return userDetail.getTotalWeightLifted() >= condition.getConditionValue();
            case "totalMountainsClimbed":
                return userDetail.getTotalMountainsClimbed() >= condition.getConditionValue();
            case "consecutiveWorkoutDays":
                return userDetail.getConsecutiveWorkoutDays() >= condition.getConditionValue();
            case "yogaSessionsCompleted":
                return userDetail.getYogaSessionsCompleted() >= condition.getConditionValue();
            case "totalDistanceCovered":
                return userDetail.getTotalDistanceCovered() >= condition.getConditionValue();
            default:
                throw new IllegalArgumentException("알 수 없는 배지 조건: " + condition.getBadgeCondition());
        }
    }
}
