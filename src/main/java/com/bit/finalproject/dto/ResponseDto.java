package com.bit.finalproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ResponseDto<T> {
    private T item;
    private List<T> items;
    private Map<String, List<SearchDto>> itemsMap;
    private Page<T> pageItems;
    private int statusCode;
    private String statusMessage;
}
