package com.bit.finalproject.service;

import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.User;
import java.util.List;

public interface CommentLikeService {

    void likeComment(Long commentId, Long userId);

    void unlikeComment(Long commentId, Long userId);

}
