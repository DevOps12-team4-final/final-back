package com.bit.finalproject.service;

import java.util.List;

public interface BlockService {
    void blockUser(Long userId, Long blockedUserId);

    void unblockUser(Long userId, Long blockedUserId);

    List<Long> getBlockedUserIds(Long userId);
}
