package com.bit.finalproject.service.impl;

import com.bit.finalproject.entity.DeletionRequest;
import com.bit.finalproject.repository.DeletionRequestRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.DeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeletionServiceImpl implements DeletionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeletionRequestRepository deletionRequestRepository;

    // 3개월(90일)마다 실행
    @Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 자정에 실행
    @Override
    public void processDeletionRequests() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        // 3개월 이상 경과한 삭제 요청 찾기
        List<DeletionRequest> requests = deletionRequestRepository.findByRequestTimeBefore(threeMonthsAgo);

        for (DeletionRequest request : requests) {
            // 사용자 삭제
            userRepository.deleteById(request.getUserId());
            // 삭제 요청 후 처리 (예: 로그 출력 등)
            System.out.println("User with ID " + request.getUserId() + " has been deleted.");
        }

        // 처리한 요청 삭제
        deletionRequestRepository.deleteAll(requests);
    }
}
