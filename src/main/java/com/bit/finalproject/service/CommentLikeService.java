package com.bit.finalproject.service;

import com.bit.finalproject.dto.LikeDataDto;
import com.bit.finalproject.entity.User;
import java.util.List;

public interface CommentLikeService {

    void likeComment(Long commentId, Long userId);

    void unlikeComment(Long commentId, Long userId);

    Long getLikeCount(Long commentId);  // 좋아요 개수 가져오기

    List<User> getLikedUsers(Long commentId);  // 좋아요한 사용자 목록 가져오기

    // 좋아요 개수와 사용자 목록을 동시에 반환
    LikeDataDto getLikeCountAndUsers(Long commentId);
}
