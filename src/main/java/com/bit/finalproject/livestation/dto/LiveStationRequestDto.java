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
public class LiveStationRequestDto {
     @JsonProperty("channelName")
     String channelName;

     @JsonProperty("cdn")
     CdnDto cdn;

     @JsonProperty("qualitySetId")
     int qualitySetId;

     @JsonProperty("useDvr")
     boolean useDvr;

     @JsonProperty("immediateOnAir")
     boolean immediateOnAir;

     @JsonProperty("timemachineMin")
     int timemachineMin;

     @JsonProperty("record")
     RecordDto record;

     @JsonProperty("isStreamFailOver")
     boolean isStreamFailOver;

}
