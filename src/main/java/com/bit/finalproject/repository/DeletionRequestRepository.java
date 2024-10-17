package com.bit.finalproject.repository;

import com.bit.finalproject.entity.DeletionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface DeletionRequestRepository extends JpaRepository<DeletionRequest, Long> {
    List<DeletionRequest> findByRequestTimeBefore(LocalDateTime time); // 특정 시간 이전의 요청 찾기
}
