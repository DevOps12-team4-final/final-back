package com.bit.finalproject.service;

import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {
    FeedDto post(FeedDto feedDto, MultipartFile[] uploadFiles, User user);

    List<FeedDto> getAllFeeds();

    Page<FeedDto> getAllFeedsExcludingUser(Long userId, Pageable pageable);

    Page<FeedDto> getAllFollowingFeeds(Long userId, Pageable pageable);

    FeedDto updateFeed(Long feedId, FeedDto feedDto, MultipartFile[] uploadFiles, String originFiles, User user);
}
