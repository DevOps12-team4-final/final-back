package com.bit.finalproject.repository;


import com.bit.finalproject.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Page<Feed> findByUser_UserIdNot(Long userId, Pageable pageable);
}
