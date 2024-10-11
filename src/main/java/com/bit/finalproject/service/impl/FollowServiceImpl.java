package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.FollowDto;
import com.bit.finalproject.entity.Follow;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.FollowRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.FollowService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }
    @Override
    // 팔로우 기능
    public void follow(Long memberId, Long userId) {
        // memberId와 userId로 User 엔티티를 조회합니다. (팔로우하는 사람과 팔로우되는 사람)
        User follower = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다: " + memberId));

        User following = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("팔로잉할 사용자를 찾을 수 없습니다: " + userId));

        // Follow 엔티티를 생성합니다.
        Follow follow = Follow.builder()
                .follower(follower)  // 팔로워 설정
                .following(following) // 팔로우 대상 설정
                .build();

        // 팔로우 관계를 저장합니다.
        followRepository.save(follow);
    }

    @Override
    // 언팔로우 기능
    public void unfollow(Long memberId, Long userId) {
        // memberId와 userId로 팔로우 관계를 조회합니다.
        User follower = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다: " + memberId));

        User following = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 대상을 찾을 수 없습니다: " + userId));

        // Follow 엔티티를 조회합니다.
        Follow follow = (Follow) followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 존재하지 않습니다."));

        // 팔로우 관계를 삭제합니다.
        followRepository.delete(follow);
    }


    // 팔로우하는 사람 목록 조회
    @Override
    public List<FollowDto> getFollowers(Long userId) {
        // 특정 사용자를 팔로우하는 모든 팔로워 조회
        return followRepository.findAllByFollowing_UserId(userId).stream()
                .map(follow -> FollowDto.builder()
                        .followerId(follow.getFollower().getUserId())           // 팔로워의 ID
                        .followerName(follow.getFollower().getUsername())       // 팔로워의 이름
                        .followingId(follow.getFollowing().getUserId())         // 팔로잉 대상의 ID
                        .followingName(follow.getFollowing().getUsername())     // 팔로잉 대상의 이름
                        .followingNameprofileImage(follow.getFollowing().getProfileImage())
                        .build())
                .collect(Collectors.toList());  // DTO 리스트로 변환 후 반환
    }


    @Override
    public List<FollowDto> getFollowings(Long userId) {
        // 특정 사용자가 팔로우하는 모든 사용자 조회
        return followRepository.findAllByFollowing_UserId(userId).stream()
                .map(follow -> FollowDto.builder()
                        .followerId(follow.getFollower().getUserId())           // 팔로워의 ID
                        .followerName(follow.getFollower().getUsername())       // 팔로워의 이름
                        .followingId(follow.getFollowing().getUserId())         // 팔로잉 대상의 ID
                        .followingName(follow.getFollowing().getUsername())     // 팔로잉 대상의 이름
                        .followingNameprofileImage(follow.getFollowing().getProfileImage())
                        .build())
                .collect(Collectors.toList());  // DTO 리스트로 변환 후 반환
    }

}
