package com.bit.finalproject.service.impl;

import com.bit.finalproject.common.FileUtils;
import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.dto.FeedFileDto;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.Member;
import com.bit.finalproject.repository.FeedRepository;
import com.bit.finalproject.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final FileUtils fileUtils;

    @Override
    public Page<FeedDto> post(FeedDto feedDto, MultipartFile[] uploadFiles, Member member, Pageable pageable) {

        feedDto.setRegdate(LocalDateTime.now());
        feedDto.setModdate(LocalDateTime.now());

        // feedDto의 정보를 Feed 엔티티로 변환하면서 member 정보를 같이 넘긴다.
        Feed feed = feedDto.toEntity(member);

        // 파일이 업로드 된다면 진행
        if(uploadFiles != null && uploadFiles.length > 0) {
            // uploadFiles 배열에 들어있는 각각의 파일들을 순회하며 처리한다.
            Arrays.stream(uploadFiles).forEach(multipartFile -> {
                // 원본 파일명을 확인한다.
                if (multipartFile.getOriginalFilename() != null &&
                        !multipartFile.getOriginalFilename().equalsIgnoreCase("")) {
                    FeedFileDto feedFileDto = fileUtils.parserFileInfo(multipartFile, "feed/");

                    System.out.println(10);
                    feed.getFeedFileList().add(feedFileDto.toEntity(feed));
                }
            });
        }

        feedRepository.save(feed);

        return feedRepository.findAll(pageable).map(Feed::toDto);
    }

}
