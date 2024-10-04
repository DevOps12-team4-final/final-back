package com.bit.finalproject.repository;

import com.bit.finalproject.dto.MemberDto;
import com.bit.finalproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String Email);

    long countByEmail(String Email);

    long countByNickname(String nickname);

    MemberDto findByUserId(Long userId);
}
