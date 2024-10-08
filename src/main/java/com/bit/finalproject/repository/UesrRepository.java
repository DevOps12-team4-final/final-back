package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Uesr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UesrRepository extends JpaRepository<Uesr, Long> {

    Optional<Uesr> findByEmail(String Email);

    long countByEmail(String Email);

    long countByNickname(String nickname);
}
