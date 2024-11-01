package com.bit.finalproject.repository;


import com.bit.finalproject.entity.UserDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long > {

    Optional<UserDetail> findByUser_UserId(Long userId);
}
