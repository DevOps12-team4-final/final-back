package com.bit.finalproject.service.impl;

import com.bit.finalproject.common.FileUtils;
import com.bit.finalproject.dto.BoardDto;
import com.bit.finalproject.dto.BoardFileDto;
import com.bit.finalproject.entity.Board;
import com.bit.finalproject.entity.Member;
import com.bit.finalproject.repository.BoardRepository;
import com.bit.finalproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final FileUtils fileUtils;

    @Override
    public Page<BoardDto> post(BoardDto boardDto, MultipartFile[] uploadFiles, Member member, Pageable pageable) {

        boardDto.setRegdate(LocalDateTime.now());
        boardDto.setModdate(LocalDateTime.now());

        // boardDto의 정보를 Board 엔티티로 변환하면서 member 정보를 같이 넘긴다.
        Board board = boardDto.toEntity(member);

        // 파일이 업로드 된다면 진행
        if(uploadFiles != null && uploadFiles.length > 0) {
            // uploadFiles 배열에 들어있는 각각의 파일들을 순회하며 처리한다.
            Arrays.stream(uploadFiles).forEach(multipartFile -> {
                // 원본 파일명을 확인한다.
                if (multipartFile.getOriginalFilename() != null &&
                        !multipartFile.getOriginalFilename().equalsIgnoreCase("")) {
                    BoardFileDto boardFileDto = fileUtils.parserFileInfo(multipartFile, "board/");

                    System.out.println(10);
                    board.getBoardFileList().add(boardFileDto.toEntity(board));
                }
            });
        }

        boardRepository.save(board);

        return boardRepository.findAll(pageable).map(Board::toDto);
    }

}
