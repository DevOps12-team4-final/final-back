package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    private int likeCount; // 좋아요 개수 필드

    public Feed toEntity(Member member) {
        return Feed.builder()
                .feedId(this.feedId)
                .content(this.content)
                .member(member)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .searchKeyword(this.searchKeyword)
                .searchCondition(this.searchCondition)
                .feedFileList(new ArrayList<>())
                .build();
    }
}
