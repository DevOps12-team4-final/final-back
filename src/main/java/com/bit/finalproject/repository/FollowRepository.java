package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Follow;
import com.bit.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId); // 팔로우 관계 확인
    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId); // 팔로우 관계 삭제

    List<Follow> findAllByFollowingId(Long followingId);  // 특정 사용자를 팔로우하는 사람들 조회
    List<Follow> findAllByFollowerId(Long followerId);    // 특정 사용자가 팔로우하는 사람들 조회

    Optional<Object> findByFollowerAndFollowing(User follower, User following);
}
