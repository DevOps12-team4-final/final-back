package com.bit.finalproject.service;

import com.bit.finalproject.dto.BoardDto;
import com.bit.finalproject.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
    Page<BoardDto> post(BoardDto boardDto, MultipartFile[] uploadFiles, Member member, Pageable pageable);
}
