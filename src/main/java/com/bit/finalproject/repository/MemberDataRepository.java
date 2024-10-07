package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Member;
import com.bit.finalproject.entity.MemberDtail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDataRepository extends JpaRepository<Member,MemberDtail> {
}
