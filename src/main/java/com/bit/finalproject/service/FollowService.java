package com.bit.finalproject.service;

public interface FollowService {
    void followUser(Long followerId, Long followingId);

    void unfollowUser(Long followerId, Long followingId);
}
