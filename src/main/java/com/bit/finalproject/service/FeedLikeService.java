package com.bit.finalproject.service;

public interface FeedLikeService {

    void removeLike(Long feedId, Long userId);

    int getLikeCount(Long feedId);

    void addLike(Long feedId, Long userId);
}
