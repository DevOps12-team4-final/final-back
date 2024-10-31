package com.bit.finalproject.service;

import com.bit.finalproject.dto.SearchDto;

import java.util.List;

public interface SearchService {
    List<SearchDto> searchByUser(String searchKeyword);

    List<SearchDto> searchByFeedContent(String searchKeyword);
}
