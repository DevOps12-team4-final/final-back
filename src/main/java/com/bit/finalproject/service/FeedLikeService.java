package com.bit.finalproject.service;

public interface FeedLikeService {

    void removeLike(Long FeedId, Long userId);

    int getLikeCount(Long FeedId);

    void addLike(Long FeedId, Long userId);
}
