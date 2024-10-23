package com.bit.finalproject.livestation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LiveStationInfoDto {
    private String channelId;
    private String channelName;
    private String channelStatus;
    private int cdnInstanceNo;
    private String cdnStatus;

    private String publishUrl;
    private String streamKey;

    private int lectureId;
}