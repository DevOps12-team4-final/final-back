package com.bit.boardappbackend.repository;


import com.bit.boardappbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
