package com.bit.finalproject.repository;

import com.bit.finalproject.entity.BadgeCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeConditionRepository extends JpaRepository<BadgeCondition, Long> {// BadgeId로 모든 BadgeCondition 검색
    List<BadgeCondition> findAllByBadge_BadgeId(Long badgeId);}
