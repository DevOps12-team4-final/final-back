package com.bit.finalproject.entity;

import com.bit.finalproject.dto.FeedCommentDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(
        name = "feedCommentSeqGenerator",
        sequenceName = "FEED_COMMENT_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "feedId",referencedColumnName = "feedId")
    private Feed feed;

    private String comment;
    private int depth;

    // 부모 댓글 ID는 Long 타입으로 설정
    private Long parentCommentId;  // 부모 댓글의 ID

    @Builder.Default
    private int orderNumber = 0;  // 기본값 0 설정

    @CreatedDate
    private LocalDateTime regdate;  // 등록일

    @LastModifiedDate
    private LocalDateTime moddate;  // 수정일

    private LocalDateTime deletedate;

    private String isdelete;

    // 좋아요와의 일대다 관계
    @OneToMany(mappedBy = "feedComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> likes = new ArrayList<>();


    public FeedCommentDto toDto(){
        return FeedCommentDto.builder()
                .commentId(commentId)
                .feedId(feed.getFeedId())
                .parentCommentId(parentCommentId)
                .comment(comment)
                .depth(depth)
                .orderNumber(orderNumber)
                .regdate(regdate)
                .moddate(moddate)
                .isdelete(isdelete)
                .build();
    }
}
