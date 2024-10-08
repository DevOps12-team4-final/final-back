package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.Uesr;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

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
    private List<FeedFileDto> feedFileDtoList;
    private int likeCount; // 좋아요 개수 필드

    public Feed toEntity(Uesr user) {
        return Feed.builder()
                .feedId(this.feedId)
                .content(this.content)
                .user(user)
                .regdate(this.regdate)
                .moddate(this.moddate)
//                .searchKeyword(this.searchKeyword)
//                .searchCondition(this.searchCondition)
                .feedFileList(new HashSet<>())
                .build();
    }
}
