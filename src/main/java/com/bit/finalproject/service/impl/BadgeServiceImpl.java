package com.bit.finalproject.service.impl;


import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.entity.UserDetail;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.repository.BadgeRepository;
import com.bit.finalproject.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class BadgeServiceImpl implements BadgeService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;





    @Override
    public boolean checkCondition(UserDetail user, Badge badge) {
        // 배지 조건 확인 로직 (운동 횟수, 도전 과제 달성 여부 등 체크)
        // 해당 로직은 Badge와 UserDetail의 상태에 따라 커스텀으로 작성 가능
        return true; // 예시로 true 반환
    }

    @Override
    public void checkAndRewardBadge(Long userId) {
        // 사용자에 대한 배지 조건을 체크하고, 조건이 충족되면 배지를 지급하는 로직을 구현
        // 예: badgeConditionChecker.checkConditionsForUser(userId);
    }
}
