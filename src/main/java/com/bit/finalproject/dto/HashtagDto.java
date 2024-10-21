package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Hashtag;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HashtagDto {

    private String hashtag;
    private Long hashtagId;

    public Hashtag toEntity() {
        return Hashtag.builder()
                .hashtag(this.hashtag)
                .hashtagId(this.hashtagId)
                .build();
    }

}
