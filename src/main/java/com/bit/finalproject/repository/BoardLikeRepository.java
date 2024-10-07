package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Board;
import com.bit.finalproject.entity.BoardLike;
import com.bit.finalproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    // 특정 게시글 좋아요 개수 반환
    int countByBoard(Board board);

    // 특정 게시글과 사용자의 좋아요 여부 확인
    boolean existsByBoardAndMember(Board board, Member member);

    // 특정 게시글과 사용자의 좋아요 찾기
    Optional<BoardLike> findByBoardAndMember(Board board, Member member);
}
