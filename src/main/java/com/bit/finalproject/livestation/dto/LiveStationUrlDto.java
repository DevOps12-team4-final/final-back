package com.bit.finalproject.livestation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LiveStationUrlDto {
    private int lectureId;
    private String channelId;
    private String name;
    private String url;
}