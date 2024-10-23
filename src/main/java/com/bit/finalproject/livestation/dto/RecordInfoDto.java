package com.bit.finalproject.livestation.dto;

import lombok.Data;

@Data
public class RecordInfoDto {
    private String recordType;
    private String channelId;
    private String status;
    private long recordSeq;
    private long streamSeq;
    private long recordBeginTime;
    private long createdTime;
    private long shouldDeleteTime;
    private String resolution;
    private String fileName;
    private long duration;
    private long videoBitrate;
    private long audioBitrate;
    private double videoFrameRate;
    private String audioCodec;
    private String uploadPath;
    private String objectStorageUrl;
}