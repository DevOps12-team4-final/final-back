package com.bit.finalproject.service.impl;

import com.bit.finalproject.common.FileUtils;
import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.dto.FeedFileDto;
import com.bit.finalproject.dto.FeedHashtagDto;

import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedHashtag;
import com.bit.finalproject.entity.Hashtag;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.FeedHashtagRepository;
import com.bit.finalproject.repository.FeedRepository;
import com.bit.finalproject.repository.HashtagRepository;
import com.bit.finalproject.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final FileUtils fileUtils;
    private final FeedHashtagRepository feedHashtagRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public FeedDto post(FeedDto feedDto, MultipartFile[] uploadFiles, User user) {

        feedDto.setRegdate(LocalDateTime.now());
        feedDto.setModdate(LocalDateTime.now());
//        System.out.println("@@@@@@@@@@@@@@@@@@@@@feedDto = " + feedDto.getFeedHashtags());
        // 해시태그가 있으면 처리

        List<String> hashtagNames = feedDto.getFeedHashtags().stream()
                .map(FeedHashtagDto::getHashtag)
                .collect(Collectors.toList());

        List<Hashtag> hashtags = hashtagNames.stream()
                .map(tag -> {
                    Optional<Hashtag> existingTag = hashtagRepository.findByHashtag(tag);
                    if (existingTag.isPresent()) {

                        return existingTag.get();
                    } else {
                        Hashtag newHashtag = new Hashtag();
                        newHashtag.setHashtag(tag);
                        hashtagRepository.save(newHashtag);

                        return newHashtag;
                    }
                })
                .collect(Collectors.toList());

        // feedDto를 Feed 엔티티로 변환하면서 사용자 정보와 해시태그도 포함
        Feed feed = feedDto.toEntity(user, hashtags);
//        System.out.println("feed = " + feed);
        // Feed 저장 (Feed는 Hashtag와 관계 없이 먼저 저장)
        Feed feedhash = feedDto.toEntity(user, new ArrayList<>()); // 일단 빈 해시태그 리스트로 저장
        feedRepository.save(feedhash); // Feed 먼저 저장

        // FeedHashtag 생성 및 저장
        List<FeedHashtag> feedHashtags = hashtags.stream()
                .map(hashtag -> FeedHashtag.builder()
                        .feed(feed)      // Feed와 Hashtag 연결
                        .hashtag(hashtag)
                        .build())
                .collect(Collectors.toList());

        feedHashtagRepository.saveAll(feedHashtags); // FeedHashtag 저장

        feedHashtagRepository.saveAll(feed.getFeedHashtags());
        System.out.println("@@@@@@@@@@@@@@@@@@@@@feed = " + feed.getFeedHashtags());

        feedRepository.save(feed); // Feed 저장


        // 파일이 업로드 된다면 파일 처리 로직
        if (uploadFiles != null && uploadFiles.length > 0) {
            Arrays.stream(uploadFiles).forEach(multipartFile -> {
                if (multipartFile.getOriginalFilename() != null &&
                        !multipartFile.getOriginalFilename().equalsIgnoreCase("")) {
                    FeedFileDto feedFileDto = fileUtils.parserFileInfo(multipartFile, "feed/");
                    // filestatus와 newfilename 설정
                    feedFileDto.setFilestatus("uploaded");  // 파일이 업로드됨
                    feedFileDto.setNewfilename(feedFileDto.getFilename());  // 새 파일명 설
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
//        return feedRepository.findAll(pageable).map(Feed::toDto);
    }

    @Override
    public List<Feed> searchFeedsByHashtag(String hashtag) {
        // 먼저 해당 해시태그가 존재하는지 확인
        Optional<Hashtag> optionalHashtag = feedHashtagRepository.findByHashtag_Hashtag(hashtag);

        // 해시태그가 존재하지 않으면 빈 리스트 반환
        if (!optionalHashtag.isPresent()) {
            return Collections.emptyList();
        }

        // 해시태그에 해당하는 FeedHashtag 리스트 가져오기
        List<FeedHashtag> feedHashtags = feedHashtagRepository.findByHashtag(optionalHashtag.get());

        // FeedHashtag 객체에서 Feed 객체를 추출하여 리스트로 반환
        return feedHashtags.stream()
                .map(FeedHashtag::getFeed)
                .collect(Collectors.toList());
    }
}
