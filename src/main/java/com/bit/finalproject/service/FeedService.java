package com.bit.finalproject.service;

import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface FeedService {
    Page<FeedDto> post(FeedDto feedDto, MultipartFile[] uploadFiles, Member member, Pageable pageable);
}
