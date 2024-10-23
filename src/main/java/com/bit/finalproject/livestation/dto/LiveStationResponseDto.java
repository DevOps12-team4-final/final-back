package com.bit.finalproject.livestation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LiveStationResponseDto {
    @JsonProperty("content")
    private ContentDto content;
    @JsonProperty("backupStreamKey")
    private String backupStreamKey;
    @JsonProperty("isStreamFailOver")
    private boolean isStreamFailOver;
}