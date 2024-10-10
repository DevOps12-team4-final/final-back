package com.bit.finalproject.service;

import com.bit.finalproject.dto.FollowDto;

public interface FollowService {
    void follow(FollowDto followDto);

    void unfollow(FollowDto followDto);
}
