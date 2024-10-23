package com.bit.finalproject.livestation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RecordResponseDto {
    @JsonProperty("content")
    private RecordContentDto contentDTO;
    private int total;
}