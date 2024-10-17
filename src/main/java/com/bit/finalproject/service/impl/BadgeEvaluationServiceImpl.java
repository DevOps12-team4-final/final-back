package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.BadgeConditionDto;
import com.bit.finalproject.dto.BadgeDto;
import com.bit.finalproject.entity.Badge;
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
    private final BadgeRepository badgeRepository;


    // 특정 배지의 조건을 유저의 기록과 비교하여 트루/펄스를 반환하는 메서드
    @Override
    public boolean evaluateBadge(Long userId, Long badgeId, UserDetail userDetail) {
        // DB에서 해당 배지의 조건을 가져옴
        List<BadgeCondition> badgeConditions = badgeConditionRepository.findAllByBadgeId(badgeId);

        // 유저의 운동 기록과 배지 조건을 비교
        return badgeConditions.stream().allMatch(condition -> evaluateCondition(userDetail, condition));
    }

    @Override
    public BadgeDto addBadge(BadgeDto badgeDto , BadgeConditionDto conditionDto) {
        // BadgeDto를 Badge 엔티티로 변환
        Badge badge = badgeDto.toEntity(); // 필요시 user를 추가할 수 있습니다
        BadgeCondition badgeCondition = conditionDto.toEntity(badge);
        // Badge 저장
        Badge savedBadge = badgeRepository.save(badge);

        // 배지 조건이 반드시 들어와야 하므로, null 체크를 하지 않고 반복문 진행
        for (BadgeConditionDto badgeConditionDto : badgeDto.getBadgeConditions()) {
            // BadgeConditionDto를 BadgeCondition 엔티티로 변환
            BadgeCondition badgeCondition = badgeConditionDto.toEntity(badge);

            // BadgeCondition 저장
            badgeConditionRepository.save()
        }

        // 저장된 배지를 DTO로 변환하여 반환
        return BadgeDto.builder()
                .badgeId(savedBadge.getBadgeId())
                .badgeName(savedBadge.getBadgeName())
                .badgeContent(savedBadge.getBadgeContent())
                .badgeGrade(savedBadge.getBadgeGrade())
                .badgeImage(savedBadge.getBadgeImage())
                .badgeConditions(badgeDto.getBadgeConditions()) // 저장된 조건 추가
                .build();
    }




    // 개별 조건을 평가하는 메서드
    @Override
    public boolean evaluateCondition(UserDetail userDetail, BadgeCondition condition) {
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
