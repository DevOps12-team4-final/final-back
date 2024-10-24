package com.bit.finalproject.service;

import com.bit.finalproject.dto.FeedDto;
<<<<<<< HEAD
import com.bit.finalproject.entity.CustomUserDetails;
=======
import com.bit.finalproject.entity.Feed;
>>>>>>> 35a1433166b1f1cd73cb567dd787c98e3a848c70
import com.bit.finalproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import java.util.List;

public interface FeedService {
    FeedDto post(FeedDto feedDto, MultipartFile[] uploadFiles, User user);

    List<FeedDto> getAllFeeds();

    Page<FeedDto> getAllFeedsExcludingUser(Long userId, Pageable pageable);

<<<<<<< HEAD
    Page<FeedDto> getAllFollowingFeeds(Long userId, Pageable pageable);
=======
    Page<FeedDto> getAllFeedsExcludingUserP(Long userId, Pageable pageable);
//    Page<FeedDto> post(FeedDto feedDto, MultipartFile[] uploadFiles, User user, Pageable pageable);

    // 해시태그를 이용해 관련된 게시글 찾기
    List<Feed> searchFeedsByHashtag(String hashtag);
>>>>>>> 35a1433166b1f1cd73cb567dd787c98e3a848c70
}
