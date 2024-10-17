package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedHashtag;
import com.bit.finalproject.entity.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedHashtagDto {

    private Long hashId;
    private Long feedId; // Feed 엔티티의 ID
    private String hashtag; // 해시태그 문자열

    // FeedHashtagDto에서 FeedHashtag 엔티티로 변환하는 메소드
    public FeedHashtag toEntity(Feed feed, Hashtag hashtagEntity) {
        return FeedHashtag.builder()
                .feed(feed)
                .hashtag(hashtagEntity)
                .build();
    }
}