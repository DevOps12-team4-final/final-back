package com.bit.finalproject.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SearchDto {

    private String searchCondition;  // 검색 조건 (예: ACCOUNT, TAG 등)
    private String searchKeyword;   // 검색 키워드
    private String nickname;


}
