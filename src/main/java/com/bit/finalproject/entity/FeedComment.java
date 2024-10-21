package com.bit.finalproject.entity;

import com.bit.finalproject.dto.FeedCommentDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class FeedComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Column(nullable = false, columnDefinition = "int default 0")
    private int orderNumber;

    @CreatedDate
    private LocalDateTime regdate;  // 등록일

    @LastModifiedDate
    private LocalDateTime moddate;  // 수정일

    private LocalDateTime deletedate;

    private String isdelete;

    // 좋아요와의 일대다 관계
    @OneToMany(mappedBy = "feedComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // 직렬화할 때 순환 참조를 방지
    @Builder.Default
    private List<CommentLike> commentLikes = new ArrayList<>();



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
