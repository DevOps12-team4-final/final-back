package com.bit.finalproject.entity;

import com.bit.finalproject.dto.FeedFileDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "feedFileSeqGenerator",
        sequenceName = "FEED_FILE_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedFile {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "feedFileSeqGenerator"
    )
    private Long feedFile_id;

    // feedFile 엔티티가 feed 엔티티와 다대일 관계
    // 여러개의 게시물파일이이 하나의 게시물에 연결될 수 있음
    // feedFile의 feed_id(외래키)를 feed의 feed_id(외래키 참조)와 조인한다.
    @ManyToOne
    @JoinColumn(name = "feedId")
    @JsonBackReference
    private Feed feed;

    private String filename;
    private String filepath;
    private String fileoriginname;
    private String filetype;
    @Transient
    private String filestatus;
    @Transient
    private String newfilename;

    public FeedFileDto toDto() {
        return FeedFileDto.builder()
                .feedFile_id(this.feedFile_id)
                .feedId(this.feed.getFeedId())
                .filename(this.filename)
                .filepath(this.filepath)
                .filetype(this.filetype)
                .fileoriginname(this.fileoriginname)
                .filestatus(this.filestatus)
                .newfilename(this.newfilename)
                .build();
    }









}
