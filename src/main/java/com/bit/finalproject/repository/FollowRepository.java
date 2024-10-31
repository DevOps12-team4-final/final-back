package com.bit.finalproject.repository;

import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.Follow;
import com.bit.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    boolean existsByFollower_UserIdAndFollowing_UserId(Long followerId, Long followingId);

    void deleteByFollower_UserIdAndFollowing_UserId(Long followerId, Long followingId);

    @Query("SELECT f.following.userId FROM Follow f WHERE f.follower.userId = :userId")
    List<Long> findFollowingUserIdsByFollowerUserId(@Param("userId") Long userId);
}
