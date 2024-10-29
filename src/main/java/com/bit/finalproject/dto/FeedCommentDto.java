package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedComment;

import com.bit.finalproject.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FeedCommentDto {

    private Long commentId;
    private Long feedId;
    private Long userId;
    private String comment;
    private Long parentCommentId;  // 부모 댓글 ID
    private int depth;
    private int orderNumber;
    private LocalDateTime regdate;  // 등록일
    private LocalDateTime moddate;  // 수정일
    private LocalDateTime deletedate;
    private String isdelete;

    // DTO에서 엔티티로 변환하는 메서드
    public FeedComment toEntity(User user, Feed feed) {
        return FeedComment.builder()
                .commentId(this.commentId)
                .feed(feed)
                .user(user)
                .parentCommentId(this.parentCommentId) // 부모 댓글 엔티티 설정
                .comment(this.comment)
                .depth(this.depth)
                .orderNumber(this.orderNumber)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .deletedate(this.deletedate)
                .isdelete(this.isdelete)
                .build();
    }


}