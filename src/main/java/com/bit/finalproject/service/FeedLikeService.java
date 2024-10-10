package com.bit.finalproject.service;

public interface FeedLikeService {

    void removeLike(Long feedId, Long memberId);

    int getLikeCount(Long feedId);

    void addLike(Long feedId, Long memberId);
}
