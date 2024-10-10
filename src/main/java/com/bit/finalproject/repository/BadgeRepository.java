package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    // 배지 정보를 DB에서 조회하는 기본 메서드 제공
}
