package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.StatisticsDto;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.DeletionRequestRepository;
import com.bit.finalproject.repository.UserDetailRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final DeletionRequestRepository deletionRequestRepository; // 삭제 요청 레포지토리 추가

    @Override
    public StatisticsDto getStatisticsSummary() {
        // 총 사용자 수, 신규 가입자 수, 활성 사용자 수 계산
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        long totalUsers = userRepository.count(); // 총 사용자 수
        long newUsers = userRepository.countNewUsersSince(thirtyDaysAgo); // 최근 30일 내에 가입한 사용자 수
        long activeUsers = userRepository.countActiveUsersSince(thirtyDaysAgo); // 최근 30일 내에 로그인한 사용자 수

        StatisticsDto dto = new StatisticsDto();
        dto.setTotalUsers((int) totalUsers);
        dto.setNewUsers((int) newUsers);
        dto.setActiveUsers((int) activeUsers);
        return dto;
    }
    // ACTIVE 상태인 유저 목록을 가져오는 메소드
    @Override
    public List<User> getActiveUsers() {
        return userRepository.findByUserStatus("ACTIVE");
    }
@Override
    // ACTIVE 상태인 유저 수를 세는 메소드
    public long countActiveUsers() {
        return userRepository.countByUserStatus("ACTIVE");
    }
}
