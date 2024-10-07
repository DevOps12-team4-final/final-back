package com.bit.finalproject.service.impl;

import com.bit.finalproject.entity.Board;
import com.bit.finalproject.entity.BoardLike;
import com.bit.finalproject.entity.Member;
import com.bit.finalproject.repository.BoardLikeRepository;
import com.bit.finalproject.repository.BoardRepository;
import com.bit.finalproject.repository.MemberRepository;
import com.bit.finalproject.service.BoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 좋아요 추가
    @Override
    public void addLike(Long boardId, Long memberId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid boardId:" + boardId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memberId: " + memberId));

        // 이미 좋아요를 눌렀는지 확인
        if (boardLikeRepository.existsByBoardAndMember(board, member)) {
            throw new IllegalArgumentException("User has already liked this post.");
        }

        // 좋아요 추가
        BoardLike boardLike = BoardLike.builder()
                .board(board)
                .member(member)
                .build();
        boardLikeRepository.save(boardLike);
    }

    // 좋아요 삭제
    @Override
    public void removeLike(Long boardId, Long memberId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid boardId:" + boardId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memberId: " + memberId));

        // 좋아요 찾기
        BoardLike boardLike = boardLikeRepository.findByBoardAndMember(board, member)
                .orElseThrow(() -> new IllegalArgumentException("Like not found."));

        // 좋아요 삭제
        boardLikeRepository.delete(boardLike);
    }

    // 게시글의 좋아요 총 개수 가져오기
    @Override
    public int getLikeCount(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid boardId:" + boardId));

        // 좋아요 개수 반환
        return boardLikeRepository.countByBoard(board);
    }
}
