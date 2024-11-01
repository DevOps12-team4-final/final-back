package com.bit.finalproject.service;

import com.bit.finalproject.dto.StatisticsDto;
import com.bit.finalproject.entity.User;

import java.util.List;

public interface StatisticsService {
    StatisticsDto getStatisticsSummary();

    // ACTIVE 상태인 유저 목록을 가져오는 메소드
    List<User> getActiveUsers();
    // ACTIVE 상태인 유저 수를 세는 메소드
    long countActiveUsers();
}
