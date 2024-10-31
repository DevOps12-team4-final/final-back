package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.User;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private List<FeedFileDto> feedFileDtoList;
    private int likeCount; // 좋아요 개수 필드
    private boolean isLiked;
    private boolean isMarked;
    private boolean isFollowing;

    // FeedDto를 Feed 엔티티로 변환하는 toEntity 메서드
    public Feed toEntity(User user) {
        return Feed.builder()
                .feedId(this.feedId)
                .content(this.content)
                .user(user)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .profileImage(this.profileImage)
                .isFollowing(this.isFollowing)
//                .searchKeyword(this.searchKeyword)
//                .searchCondition(this.searchCondition)
                .feedFileList(new HashSet<>())
                .build();
    }
}
