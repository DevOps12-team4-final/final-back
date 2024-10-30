package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedHashtag;
import com.bit.finalproject.entity.Hashtag;
import com.bit.finalproject.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FeedDto {
    private Long feedId;
    private String content;
    private Long userId;
    private String nickname;
    private LocalDateTime regdate;  // 등록일
    private LocalDateTime moddate;  // 수정일
//    private String searchKeyword;
//    private String searchCondition;
    private String profileImage;
    private List<FeedHashtagDto> feedHashtags;
    private List<FeedFileDto> feedFileDtoList;
    private int likeCount; // 좋아요 개수 필드
    private boolean isLiked;
    private boolean isMarked;
    private boolean isFollowing;

    // FeedDto를 Feed 엔티티로 변환하는 toEntity 메서드
    public Feed toEntity(User user,  List<Hashtag> hashtags) {
        Feed feed = Feed.builder()
                .feedId(this.feedId)
                .content(this.content)
                .user(user)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .profileImage(this.profileImage)
                .isFollowing(this.isFollowing)
                .feedFileList(new HashSet<>())
                .build();

        List<FeedHashtag> feedHashtags = hashtags.stream()
                .map(hashtag -> FeedHashtag.builder()
                        .feed(feed) // 변환된 Feed 객체 사용
                        .hashtag(hashtag) // 매핑된 Hashtag 객체 추가
                        .build())
                .collect(Collectors.toList());
        // Feed 객체에 FeedHashtag 리스트 추가
        feed.setFeedHashtags(feedHashtags);
        return feed;
    }
}
