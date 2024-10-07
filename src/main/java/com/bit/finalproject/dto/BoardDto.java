package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Board;
import com.bit.finalproject.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardDto {
    private Long board_id;
    private String content;
    private Long user_id;
    private String nickname;
    private LocalDateTime regdate;  // 등록일
    private LocalDateTime moddate;  // 수정일
    private String searchKeyword;
    private String searchCondition;
    private List<BoardFileDto> boardFileDtoList;
    private int likeCount; // 좋아요 개수 필드

    public Board toEntity(Member member) {
        return Board.builder()
                .board_id(this.board_id)
                .content(this.content)
                .member(member)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .searchKeyword(this.searchKeyword)
                .searchCondition(this.searchCondition)
                .boardFileList(new ArrayList<>())
                .build();
    }
}
