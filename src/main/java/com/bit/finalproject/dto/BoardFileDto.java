package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Board;
import com.bit.finalproject.entity.BoardFile;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardFileDto {
    private Long boardFile_id;
    private Long board_id;
    private String filename;
    private String filepath;
    private String fileoriginname;
    private String filetype;
    private String filestatus;
    private String newfilename;

    public BoardFile toEntity(Board board) {
        return BoardFile.builder()
                .boardFile_id(this.boardFile_id)
                .board(board)
                .filename(this.filename)
                .filepath(this.filepath)
                .fileoriginname(this.fileoriginname)
                .filetype(this.filetype)
                .filestatus(this.filestatus)
                .newfilename(this.newfilename)
                .build();
    }
}