package com.bit.finalproject.service;

import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.entity.Uesr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {
    Page<FeedDto> post(FeedDto feedDto, MultipartFile[] uploadFiles, Uesr user, Pageable pageable);

    List<FeedDto> getAllFeeds();
}
