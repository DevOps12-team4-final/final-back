package com.bit.finalproject.service;

import com.bit.finalproject.dto.FeedDto;

import java.util.List;

public interface BookMarkService {
    void addBookMark(Long feedId, Long userId);

    void removeBookMark(Long feedId, Long userId);

    List<FeedDto> getBookmarks(Long userId);
}
