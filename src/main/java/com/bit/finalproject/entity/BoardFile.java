package com.bit.finalproject.entity;

import com.bit.finalproject.dto.BoardFileDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "boardFileSeqGenerator",
        sequenceName = "BOARD_FILE_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFile {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "boardFileSeqGenerator"
    )
    private Long boardFile_id;

    // boardFile 엔티티가 board 엔티티와 다대일 관계
    // 여러개의 게시물파일이이 하나의 게시물에 연결될 수 있음
    // boardFile의 board_id(외래키)를 board의 board_id(외래키 참조)와 조인한다.
    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference
    private Board board;

    private String filename;
    private String filepath;
    private String fileoriginname;
    private String filetype;
    @Transient
    private String filestatus;
    @Transient
    private String newfilename;

    public BoardFileDto toDto() {
        return BoardFileDto.builder()
                .boardFile_id(this.boardFile_id)
                .board_id(this.board.getBoard_id())
                .filename(this.filename)
                .filepath(this.filepath)
                .filetype(this.filetype)
                .fileoriginname(this.fileoriginname)
                .filestatus(this.filestatus)
                .newfilename(this.newfilename)
                .build();
    }









}
