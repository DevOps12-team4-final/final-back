package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.FeedCommentDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedComment;
import com.bit.finalproject.entity.Member;
import com.bit.finalproject.repository.FeedCommentRepository;
import com.bit.finalproject.repository.FeedRepository;
import com.bit.finalproject.repository.MemberRepository;
import com.bit.finalproject.service.FeedCommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;

@Service
@AllArgsConstructor
public class FeedCommentServiceImpl implements FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;
    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;

    @Override
    public FeedCommentDto createComment(FeedCommentDto feedCommentDto) {
        // Feed 엔티티 설정
        Feed feed = feedRepository.findById(feedCommentDto.getFeedId())
                .orElseThrow(() -> new IllegalArgumentException("Feed not found with id: " + feedCommentDto.getFeedId()));

        // FeedComment 엔티티 생성
        FeedComment feedComment = new FeedComment();
        feedComment.setComment(feedCommentDto.getComment());
        feedComment.setFeed(feed);  // Feed 엔티티 설정

        // Member 정보 설정
        feedComment.setMember(memberRepository.findById(feedCommentDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"))
        );

        // 부모 댓글이 있는지 확인 후 처리
        if (feedCommentDto.getParentCommentId() != null) {
            // parentCommentId로 부모 댓글 가져오기
            FeedComment parentComment = feedCommentRepository.findById(feedCommentDto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found with id: " + feedCommentDto.getParentCommentId()));

            // 부모 댓글의 depth에 +1
            feedComment.setDepth(parentComment.getDepth() + 1);
            // 부모 댓글 ID 설정
            feedComment.setParentCommentId(parentComment.getCommentId());  // 여기서 부모 댓글의 commentId를 사용
        } else {
            // 원댓글인 경우
            feedComment.setDepth(0);  // 깊이 0 설정
        }

        // 댓글 기본 정보 설정
        feedComment.setRegdate(LocalDateTime.now());
        feedComment.setIsdelete("F"); // 댓글은 처음 생성할 때 삭제 상태가 아님

        // 댓글 저장
        FeedComment savedComment = feedCommentRepository.save(feedComment);

        // 저장된 댓글을 FeedCommentDto로 변환하여 반환
        FeedCommentDto savedCommentDto = new FeedCommentDto();
        savedCommentDto.setFeedId(savedComment.getFeed().getFeedId());  // Feed ID 설정
        savedCommentDto.setCommentId(savedComment.getCommentId());  // Comment ID 설정
        savedCommentDto.setUserId(savedComment.getMember().getUserId());  // User ID 설정
        savedCommentDto.setComment(savedComment.getComment());  // Comment 설정
        savedCommentDto.setRegdate(savedComment.getRegdate());  // 등록일 설정
        savedCommentDto.setModdate(savedComment.getModdate());  // 수정일 설정
        savedCommentDto.setIsdelete(savedComment.getIsdelete());  // 삭제 여부 설정
        savedCommentDto.setDepth(savedComment.getDepth());  // Depth 설정

        // 부모 댓글 ID 설정
        if (savedComment.getParentCommentId() != null) {
            savedCommentDto.setParentCommentId(savedComment.getParentCommentId());
        }

        return savedCommentDto;
    }
}