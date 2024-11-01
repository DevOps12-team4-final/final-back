package com.bit.finalproject.controller;

import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.dto.SearchDto;
import com.bit.finalproject.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<ResponseDto<?>> getSearch(@RequestParam("searchKeyword") String searchKeyword) {
        ResponseDto<Map<String, List<SearchDto>>> responseDto = new ResponseDto<>();
        Map<String, List<SearchDto>> results = new HashMap<>();

        try {
            // 모든 카테고리에서 검색 실행
            results.put("USER", searchService.searchByUser(searchKeyword));
            results.put("FEED", searchService.searchByFeedContent(searchKeyword));

            responseDto.setItemsMap(results);  // 카테고리별 검색 결과 설정
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Search completed successfully.");

            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            // 잘못된 검색 조건에 대한 예외 처리
            responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDto);
        } catch (Exception e) {
            // 기타 예외 처리
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("An error occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
}