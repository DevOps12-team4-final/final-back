package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Badge;
import com.bit.finalproject.entity.BadgeCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<BadgeCondition> findAllByBadgeId(Long badgeId);
    List<BadgeCondition> findAllByBadge_BadgeId(Long badgeId);}


