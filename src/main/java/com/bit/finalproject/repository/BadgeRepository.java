package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.BadgeCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<BadgeCondition> findAllByBadgeId(Long badgeId);
    // 배지 정보를 DB에서 조회하는 기본 메서드 제공
}
