package com.bit.finalproject.entity;

import com.bit.finalproject.dto.FeedCommentDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "feedCommentSeqGenerator"
    )
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

    @Column(nullable = false) //DB에서 null 허용 안 됨
    private int ordernumber = 0;  // 기본값 0 설정

    @CreatedDate
    private LocalDateTime regdate;  // 등록일

    @LastModifiedDate
    private LocalDateTime moddate;  // 수정일

    private LocalDateTime deletedate;

    private String isdelete;

    public FeedCommentDto toDto(){
        return FeedCommentDto.builder()
                .commentId(commentId)
                .feedId(feed.getFeedId())
                .parentCommentId(parentCommentId)
                .comment(comment)
                .depth(depth)
                .ordernumber(ordernumber)
                .regdate(regdate)
                .moddate(moddate)
                .isdelete(isdelete)
                .build();
    }
}
