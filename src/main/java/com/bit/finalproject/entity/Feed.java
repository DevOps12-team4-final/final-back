package com.bit.finalproject.entity;

import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.dto.FeedHashtagDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
//    @ManyToMany
//    @JoinTable(
//            name = "feed_hashtag",
//            joinColumns = @JoinColumn(name = "feed_id"),
//            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
//    )
//    private Set<HashTag> hashtags = new HashSet<>();

    @Column(name = "profile_image")
    private String profileImage;

    // Feed 엔티티가 user 엔티티와 다대일 관계
    // 여러개의 게시물이 하나의 회원에 연결될 수 있음
    // user 엔티티의 user_id의 값을 user_id로 Feed에 생성한다.
    @ManyToOne
    @JoinColumn(name = "user_Id", referencedColumnName = "userId")
    private User user;
    private LocalDateTime regdate;
    private LocalDateTime moddate;
    @Transient
    private String searchKeyword;
    @Transient
    private String searchCondition;
    private boolean isFollowing;

    // Feed 엔티티가 Feedfile 엔티티와 일대다 관계
    // Feed가 여러개의 Feedfile을 가질 수 있다.
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    @OrderBy("feedFileId ASC")  // feedFileId를 기준으로 오름차순 정렬
    @JsonManagedReference
    private Set<FeedFile> feedFileList;

    // 좋아요와 일대다 관계
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private Set<FeedLike> likes;

    // 피드해시태그와 일대다 관계
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FeedHashtag> feedHashtags = new ArrayList<>();

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
                .profileImage(this.user.getProfileImage())
                .nickname(this.user.getNickname())
                .regdate(this.regdate)
                .moddate(this.moddate)
                .isFollowing(this.isFollowing)
//                .searchKeyword(this.searchKeyword)
//                .searchCondition(this.searchCondition)
                .feedFileDtoList(
                        feedFileList != null && feedFileList.size() > 0
                                ? feedFileList.stream().map(FeedFile::toDto).toList()
                                : new ArrayList<>()
                ) .feedHashtags(this.feedHashtags != null && !this.feedHashtags.isEmpty()
                        ? this.feedHashtags.stream()
                        .map(feedHashtag -> FeedHashtagDto.builder()
                                .hashId(feedHashtag.getHashId())
                                .hashtag(feedHashtag.getHashtag().getHashtag()) // Hashtag 문자열 추가
                                .build())
                        .collect(Collectors.toList())
                        : new ArrayList<>()) // null인 경우 빈 리스트로 처리
                .likeCount(this.getLikeCount())  // 좋아요 개수 추가
                .build();
    }













}
