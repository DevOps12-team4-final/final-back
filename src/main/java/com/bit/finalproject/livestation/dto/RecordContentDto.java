package com.bit.finalproject.livestation.dto;

import lombok.Data;

import java.util.Map;

@Data
public class RecordContentDto {
    private Map<String, RecordInfoDto> recordList;
}