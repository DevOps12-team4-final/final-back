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
public class AlertOptionsDto {
    @JsonProperty("alertChangeStatus")
    private boolean alertChangeStatus;

    @JsonProperty("alertVodUploadFail")
    private boolean alertVodUploadFail;

    @JsonProperty("alertReStreamFail")
    private boolean alertReStreamFail;
}