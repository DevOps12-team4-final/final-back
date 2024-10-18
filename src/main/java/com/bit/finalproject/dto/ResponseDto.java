package com.bit.finalproject.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class ResponseDto<T> {
    private T item;
    private List<T> items;
    private Page<T> pageItems;
    private int statusCode;
    private String statusMessage;
}
