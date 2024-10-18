package com.bit.finalproject.repository;

import com.bit.finalproject.entity.BanHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface BanHistoryRepository extends JpaRepository<BanHistory, Integer> {
    List<BanHistory> findAll();
}
