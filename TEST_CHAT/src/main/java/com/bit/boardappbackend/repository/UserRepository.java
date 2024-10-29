package com.bit.finalproject.repository;


import com.bit.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
