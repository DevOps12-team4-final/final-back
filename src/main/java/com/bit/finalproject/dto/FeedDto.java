package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedHashtag;
import com.bit.finalproject.entity.Hashtag;
import com.bit.finalproject.entity.User;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
<<<<<<< HEAD
import java.util.Set;
=======
import java.util.stream.Collectors;
>>>>>>> 35a1433166b1f1cd73cb567dd787c98e3a848c70

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
//    private String searchKeyword;
//    private String searchCondition;
//    private List<FeedFileDto> feeddFileDtoList;
    private List<FeedHashtagDto> feedHashtags; // 해시태그 리스트 필드

    private int likeCount; // 좋아요 개수 필드
    private boolean isFollowing;

    public Feed toEntity(User user, List<Hashtag> hashtags) {
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
//                .searchKeyword(this.searchKeyword)
//                .searchCondition(this.searchCondition)
//                .feedFileList(new ArrayList<>())
                .feedHashtags(new ArrayList<>())
                .build();
    }
}
