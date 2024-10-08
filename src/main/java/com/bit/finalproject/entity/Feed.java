package com.bit.finalproject.entity;

import com.bit.finalproject.dto.FeedDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

@Entity
@SequenceGenerator(
        name = "feedSeqGenerator",
        sequenceName = "Feed_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feed {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "feedSeqGenerator"
    )
    private Long feedId;
    private String content;

    // Feed 엔티티가 user 엔티티와 다대일 관계
    // 여러개의 게시물이 하나의 회원에 연결될 수 있음
    // user 엔티티의 user_id의 값을 user_id로 Feed에 생성한다.
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private Uesr user;
    private LocalDateTime regdate;
    private LocalDateTime moddate;
    @Transient
    private String searchKeyword;
    @Transient
    private String searchCondition;

    // Feed 엔티티가 Feedfile 엔티티와 일대다 관계
    // Feed가 여러개의 Feedfile을 가질 수 있다.
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<FeedFile> feedFileList;

    // 좋아요와 일대다 관계
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private Set<FeedLike> likes;

    // 게시글의 좋아요 개수 반환
    public int getLikeCount() {
        if (this.likes == null) {
            return 0;  // likes가 null이면 0을 반환
        }
        return this.likes.size();  // 좋아요 개수를 계산
    }

    public FeedDto toDto() {
        return FeedDto.builder()
                .feedId(this.feedId)
                .content(this.content)
                .userId(this.user.getUserId())
                .nickname(this.user.getNickname())
                .regdate(this.regdate)
                .moddate(this.moddate)
//                .searchKeyword(this.searchKeyword)
//                .searchCondition(this.searchCondition)
                .feedFileDtoList(
                        feedFileList != null && feedFileList.size() > 0
                                ? feedFileList.stream().map(FeedFile::toDto).toList()
                                : new ArrayList<>()
                )
                .likeCount(this.getLikeCount())  // 좋아요 개수 추가
                .build();
    }













}
