package com.bit.finalproject.service;

public interface BoardLikeService {

    void removeLike(Long boardId, Long memberId);

    int getLikeCount(Long boardId);

    void addLike(Long boardId, Long memberId);
}
