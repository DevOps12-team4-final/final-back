package com.bit.finalproject.entity;

import com.bit.finalproject.dto.FeedHashtagDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FeedHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hashId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    // Feed와 Hashtag를 인자로 받는 생성자 추가
    public FeedHashtag(Feed feed, Hashtag hashtag) {
        this.feed = feed;
        this.hashtag = hashtag;
    }

    public FeedHashtagDto toDto(Feed feed, Hashtag hashtag) {
        return FeedHashtagDto.builder()
                .hashId(this.hashId)
                .hashtag(this.hashtag.getHashtag())
                .build();
    }
}
