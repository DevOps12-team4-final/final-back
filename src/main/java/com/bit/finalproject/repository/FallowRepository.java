package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Fallow;
import com.bit.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FallowRepository extends JpaRepository<Fallow, Long> {

    // 특정 회원을 팔로잉하는 팔로워의 수를 계산하는 메서드
    int countByFallowing(User fallowing);

    // 특정 회원이 팔로잉하는 팔로잉의 수를 계산하는 메서드
    int countByFallower(User fallower);
}
