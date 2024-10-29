package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedHashtag;
import com.bit.finalproject.entity.Hashtag;
import com.bit.finalproject.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Long userId;
    private String content;
    private String nickname;
    private LocalDateTime regdate;  // 등록일
    private LocalDateTime moddate;  // 수정일
    private String searchKeyword;
    private String searchCondition;
    private String profileImage;
    private List<FeedFileDto> feedFileDtoList;
    private List<FeedHashtagDto> feedHashtags; // 해시태그 리스트 필드


    private int likeCount; // 좋아요 개수 필드

    public Feed toEntity(User user, List<Hashtag> hashtags) {
        // FeedDto -> Feed 변환된 객체를 먼저 생성
        Feed feed = Feed.builder()
                .feedId(this.feedId)
                .content(this.content)
                .user(user)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .searchKeyword(this.searchKeyword)
                .searchCondition(this.searchCondition)
                .feedFileList(new HashSet<>()) // 파일 리스트도 비워둠
                .build();

        // 변환된 Feed 객체를 사용하여 FeedHashtag 리스트 생성
        List<FeedHashtag> feedHashtags = hashtags.stream()
                .map(hashtag -> FeedHashtag.builder()
                        .feed(feed) // 변환된 Feed 객체 사용
                        .hashtag(hashtag) // 매핑된 Hashtag 객체 추가
                        .build())
                .collect(Collectors.toList());

        // Feed 객체에 FeedHashtag 리스트 추가
        feed.setFeedHashtags(feedHashtags);

        return feed; // 최종적으로 Feed 객체 반환
    }
}
