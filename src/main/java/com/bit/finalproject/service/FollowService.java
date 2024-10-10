package com.bit.finalproject.service;

import com.bit.finalproject.dto.FollowDto;

import java.util.List;

public interface FollowService {
    void follow(Long followerId, Long followeeId);

    void unfollow(Long memberId, Long userId);

    List<FollowDto> getFollowers(Long userId);

    List<FollowDto> getFollowings(Long userId);
}
