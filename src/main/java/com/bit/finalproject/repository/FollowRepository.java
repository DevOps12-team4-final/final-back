package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Follow;
import com.bit.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 팔로우 관계 확인 (Follower와 Followee의 User ID 사용)
    boolean existsByFollower_UserIdAndFollowing_UserId(Long followerId, Long followingId);

    // 팔로우 관계 삭제 (Follower와 Followee의 User ID 사용)
    void deleteByFollower_UserIdAndFollowing_UserId(Long followerId, Long followingId);

    // 팔로워의 User ID로 모든 팔로우 관계 조회
    List<Follow> findAllByFollower_UserId(Long followerId);

    // 팔로잉의 User ID로 모든 팔로우 관계 조회
    List<Follow> findAllByFollowing_UserId(Long followingId);

    // Follower와 Following 객체로 팔로우 관계 조회
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    // 팔로워 수 계산 (특정 사용자를 팔로우하는 사람 수)
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.userId = :memberId")
    int countFollowers(Long memberId);

    // 팔로잉 수 계산 (특정 사용자가 팔로우하는 사람 수)
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.userId = :memberId")
    int countFollowing(Long memberId);
}

