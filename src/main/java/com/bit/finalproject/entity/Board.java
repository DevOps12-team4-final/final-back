package com.bit.finalproject.entity;

import com.bit.finalproject.dto.BoardDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(
        name = "boardSeqGenerator",
        sequenceName = "BOARD_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "boardSeqGenerator"
    )
    private Long board_id;
    private String content;

    // board 엔티티가 member 엔티티와 다대일 관계
    // 여러개의 게시물이 하나의 회원에 연결될 수 있음
    // member 엔티티의 user_id의 값을 user_id로 board에 생성한다.
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Member member;
    private LocalDateTime regdate;
    private LocalDateTime moddate;
    @Transient
    private String searchKeyword;
    @Transient
    private String searchCondition;

    // board 엔티티가 boardfile 엔티티와 일대다 관계
    // board가 여러개의 boardfile을 가질 수 있다.
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BoardFile> boardFileList;

    // 좋아요와 일대다 관계
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardLike> likes;

    // 게시글의 좋아요 개수 반환
    public int getLikeCount() {
        if (this.likes == null) {
            return 0;  // likes가 null이면 0을 반환
        }
        return this.likes.size();  // 좋아요 개수를 계산
    }


    public BoardDto toDto() {
        return BoardDto.builder()
                .board_id(this.board_id)
                .content(this.content)
                .user_id(this.member.getUser_id())
                .nickname(this.member.getNickname())
                .regdate(this.regdate)
                .moddate(this.moddate)
                .searchKeyword(this.searchKeyword)
                .searchCondition(this.searchCondition)
                .boardFileDtoList(
                        boardFileList != null && boardFileList.size() > 0
                                ? boardFileList.stream().map(BoardFile::toDto).toList()
                                : new ArrayList<>()
                )
                .likeCount(this.getLikeCount())  // 좋아요 개수 추가
                .build();
    }













}
