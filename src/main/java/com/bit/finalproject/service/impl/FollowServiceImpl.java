package com.bit.finalproject.service.impl;

import com.bit.finalproject.entity.Follow;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.FollowRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.FollowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    public void followUser(Long followerId, Long followingId) {

        // 1. 중복된 팔로우 관계 확인
        boolean isAlreadyFollowing = followRepository.existsByFollower_UserIdAndFollowing_UserId(followerId, followingId);
        if(isAlreadyFollowing){
            throw new IllegalArgumentException("이미 팔로우 중입니다.");
        }

        // 2. 사용자 정보 조회
        User follower = userRepository.findById(followerId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        User following = userRepository.findById(followingId).orElseThrow(
                () -> new IllegalArgumentException("팔로우할 사용자를 찾을 수 없습니다.")
        );

        // 3. Follow 엔티티에 저장(DB 저장)
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {

        System.out.println(followerId);
        System.out.println(followingId);
        if(followRepository.existsByFollower_UserIdAndFollowing_UserId(followerId, followingId)){
            followRepository.deleteByFollower_UserIdAndFollowing_UserId(followerId, followingId);
        } else{
            throw new IllegalArgumentException("팔로우 관계가 아닙니다.");
        }
    }
}
