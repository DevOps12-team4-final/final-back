package com.bit.finalproject.repository;

import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String Email);

    long countByEmail(String Email);

    long countByNickname(String nickname);

    UserDto findByUserId(Long userId);
}
