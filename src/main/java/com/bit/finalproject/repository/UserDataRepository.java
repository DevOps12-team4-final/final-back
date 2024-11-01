package com.bit.finalproject.repository;

import com.bit.finalproject.entity.User;
import com.bit.finalproject.entity.UserDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<User, UserDetail> {
}
