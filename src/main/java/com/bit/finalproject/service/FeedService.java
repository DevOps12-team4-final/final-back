package com.bit.finalproject.service;

import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface FeedService {
    Page<FeedDto> post(FeedDto feedDto, MultipartFile[] uploadFiles, User user, Pageable pageable);

    // 해시태그를 이용해 관련된 게시글 찾기
    List<Feed> searchFeedsByHashtag(String hashtag);

    Page<FeedDto> getAllFeedsExcludingUser(Long userId, Pageable pageable);
}
