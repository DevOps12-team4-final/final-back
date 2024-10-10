package com.bit.finalproject.controller;


import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.dto.SearchDto;
import com.bit.finalproject.service.SearchService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<ResponseDto<?>> getSearch(@RequestParam("searchCondition") String searchCondition,
                                                    @RequestParam("searchKeyword") String searchKeyword) {
        ResponseDto<SearchDto> responseDto = new ResponseDto<>();
        List<?> results;

        try {
            // 검색 조건에 따른 검색 실행
            if (searchCondition.equalsIgnoreCase("MEMBER")) {
                results = searchService.searchByUser(searchKeyword);
            } else {
                // 잘못된 검색 조건 처리
                throw new IllegalArgumentException("Invalid search condition: " + searchCondition);
            }

            // ResponseDto에 검색 결과 및 상태 정보 설정
            responseDto.setItems((List<SearchDto>) results);  // 결과 목록
            responseDto.setStatusCode(HttpStatus.OK.value());  // 200 OK
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