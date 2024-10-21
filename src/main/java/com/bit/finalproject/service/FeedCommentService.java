package com.bit.finalproject.service;

import com.bit.finalproject.dto.FeedCommentDto;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.NoSuchElementException;

public interface FeedCommentService {

    // 댓글 생성 메서드
    FeedCommentDto createComment(FeedCommentDto feedCommentDto);

    // 특정 피드의 모든 댓글 조회 메서드
    List<FeedCommentDto> findAllCommentsByFeedId(Long feedId);

    // 댓글 ID로 댓글 조회
    FeedCommentDto findById(Long commentId);

    // 댓글 업데이트
    FeedCommentDto updateComment(Long commentId, FeedCommentDto feedCommentDto);

    // 댓글 삭제
    void deleteComment(Long commentId, Long userId) throws AccessDeniedException, NoSuchElementException;
}
