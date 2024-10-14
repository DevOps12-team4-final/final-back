package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedFile;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FeedFileDto {
    private Long feedFileId;
    private Long feedId;
    private String filename;
    private String filepath;
    private String fileoriginname;
    private String filetype;
    private String filestatus;
    private String newfilename;

    public FeedFile toEntity(Feed feed) {
        return FeedFile.builder()
                .feedFileId(this.feedFileId)
                .feed(feed)
                .filename(this.filename)
                .filepath(this.filepath)
                .fileoriginname(this.fileoriginname)
                .filetype(this.filetype)
                .filestatus(this.filestatus)
                .newfilename(this.newfilename)
                .build();
    }
}