package com.bit.finalproject.entity;

import com.bit.finalproject.dto.HashtagDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hashtagId;

    private String hashtag;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    private List<FeedHashtag> feedHashtags = new ArrayList<>();

    public HashtagDto toDto() {
        return HashtagDto.builder()
                .hashtagId(this.hashtagId)
                .hashtag(this.hashtag)
                .build();


    }
}
