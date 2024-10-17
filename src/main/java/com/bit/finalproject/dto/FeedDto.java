package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedHashtag;
import com.bit.finalproject.entity.Hashtag;
import com.bit.finalproject.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private LocalDateTime regdate;  // 등록일
    private LocalDateTime moddate;  // 수정일
    private String searchKeyword;
    private String searchCondition;
    private List<FeedFileDto> feeddFileDtoList;
    private List<FeedHashtagDto> feedHashtags; // 해시태그 리스트 필드


    private int likeCount; // 좋아요 개수 필드

    public Feed toEntity(User user, List<Hashtag> hashtags) {
        return Feed.builder()
                .feedId(this.feedId)
                .content(this.content)
                .user(user)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .searchKeyword(this.searchKeyword)
                .searchCondition(this.searchCondition)
                .feedFileList(new ArrayList<>())
                .feedHashtags(new ArrayList<>())
                .build();
    }
}
