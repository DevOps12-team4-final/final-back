package com.bit.finalproject.service.impl;

import com.bit.finalproject.common.FileUtils;
import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.dto.FeedFileDto;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.FeedRepository;
import com.bit.finalproject.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final FileUtils fileUtils;

    @Override
    public FeedDto post(FeedDto feedDto, MultipartFile[] uploadFiles, User user) {

        feedDto.setRegdate(LocalDateTime.now());
        feedDto.setModdate(LocalDateTime.now());

        // FeedDto의 정보를 Feed 엔티티로 변환하면서 user 정보를 같이 넘긴다.
        Feed feed = feedDto.toEntity(user);

        // 파일이 업로드 된다면 진행
        if(uploadFiles != null && uploadFiles.length > 0) {
            // uploadFiles 배열에 들어있는 각각의 파일들을 순회하며 처리한다.
            Arrays.stream(uploadFiles).forEach(multipartFile -> {
                // 원본 파일명을 확인한다.
                if (multipartFile.getOriginalFilename() != null &&
                        !multipartFile.getOriginalFilename().equalsIgnoreCase("")) {
                    FeedFileDto feedFileDto = fileUtils.parserFileInfo(multipartFile, "feed/");

                    // filestatus와 newfilename 설정
                    feedFileDto.setFilestatus("uploaded");  // 파일이 업로드됨
                    feedFileDto.setNewfilename(feedFileDto.getFilename());  // 새 파일명 설정

                    feed.getFeedFileList().add(feedFileDto.toEntity(feed));
                }
            });
        }

        feedRepository.save(feed);

        return feed.toDto();
    }

    @Override
    public List<FeedDto> getAllFeeds() {

        // feedFile을 순서대로 가져오기위한 sort 객체 사용
        Sort sort = Sort.by(Sort.Direction.ASC, "feedFileList.feedFileId");

        // 모든 게시글 가져오기
        List<Feed> feedList = feedRepository.findAll(sort);

        // 각 게시글을 Dto로 변환하여 리스트로 반환
        return feedList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

//    @Override
//    public List<FeedDto> getAllFeedsExcludingUser(Long userId) {
//
//        // 모든 게시글 가져오기
//        List<Feed> feedList = feedRepository.findAll();
//
//        // 사용자 게시물 제외
//        List<Feed> filteredFeedList = feedList.stream()
//                .filter(feed -> !feed.getUser().getUserId().equals(userId))
//                .toList();
//
//        // 각 게시글을 Dto로 변환하여 리스트로 반환
//        return filteredFeedList.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

    @Override
    public Page<FeedDto> getAllFeedsExcludingUserP(Long userId, Pageable pageable) {

        // 모든 게시글 가져오기
        Page<Feed> feedList = feedRepository.findByUser_UserIdNot(userId, pageable);

        return feedList.map(this::convertToDto);
    }

    private FeedDto convertToDto(Feed feed) {
        FeedDto feedDto = new FeedDto();
        feedDto.setFeedId(feed.getFeedId());  // 게시글 고유 id
        feedDto.setContent(feed.getContent());    // 게시글 내용
        feedDto.setRegdate(feed.getRegdate());    // 게시글 등록일
        feedDto.setModdate(feed.getModdate());    // 게시글 수정일
        feedDto.setUserId(feed.getUser().getUserId());    // 게시글 올린 유저 id
        feedDto.setNickname(feed.getUser().getNickname());  // 게시글 올린 유저 nickname
        feedDto.setProfileImage(feed.getUser().getProfileImage());

        // 사진 파일 리스트
        List<FeedFileDto> feedFileDtoList = feed.getFeedFileList().stream()
                .map(file -> {
                    FeedFileDto feedFileDto = new FeedFileDto();
                    feedFileDto.setFeedFileId(file.getFeedFileId());   // 파일 id
                    feedFileDto.setFeedId(file.getFeed().getFeedId());// 파일의 게시글 id
                    feedFileDto.setFilename(file.getFilename());   // 파일 이름
                    feedFileDto.setFilepath(file.getFilepath());   // 파일 경로
                    feedFileDto.setFileoriginname(file.getFileoriginname());
                    feedFileDto.setFiletype(file.getFiletype());
                    feedFileDto.setFilestatus(file.getFilestatus());
                    feedFileDto.setNewfilename(file.getNewfilename());
                    return feedFileDto;
                }).toList();
        feedDto.setFeedFileDtoList(feedFileDtoList);

        // 좋아요 개수
        feedDto.setLikeCount(feed.getLikeCount());

        // 댓글 가져오는 기능 구현

        // 운동 가져오는 기능 구현

        return feedDto;
    }

}
