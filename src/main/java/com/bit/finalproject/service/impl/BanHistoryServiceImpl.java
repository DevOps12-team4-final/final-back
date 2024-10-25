package com.bit.finalproject.service.impl;

import com.bit.finalproject.entity.BanHistory;
import com.bit.finalproject.repository.BanHistoryRepository;
import com.bit.finalproject.service.BanHistoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BanHistoryServiceImpl implements BanHistoryService {


    private final BanHistoryRepository banHistoryRepository;

    public void addBanHistory(Long userId,Long banId,String reason ) {
        BanHistory banHistory = BanHistory.builder()
                .userId(userId)
                .banDate(LocalDateTime.now())
                .reason(reason) // 밴 사유를 상황에 맞게 작성
                .build();

        banHistoryRepository.save(banHistory);
    }
}
