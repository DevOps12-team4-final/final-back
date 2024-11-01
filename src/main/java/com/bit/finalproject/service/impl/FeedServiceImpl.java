package com.bit.finalproject.service.impl;

import com.bit.finalproject.common.FileUtils;
import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.dto.FeedFileDto;
import com.bit.finalproject.dto.FeedHashtagDto;
import com.bit.finalproject.dto.HashtagDto;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedHashtag;
import com.bit.finalproject.entity.Hashtag;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.*;
import com.bit.finalproject.service.FeedService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final FeedFileRepository feedFileRepository;
    private final FollowRepository followRepository;
    private final FileUtils fileUtils;
    private final FeedHashtagRepository feedHashtagRepository;
    private final HashtagRepository hashtagRepository;
    private  final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @CacheEvict(value = {"allFeeds", "feedsByUser", "feedsByHashtag"}, allEntries = true)
    public FeedDto post(FeedDto feedDto, MultipartFile[] uploadFiles, User user) {


        feedDto.setRegdate(LocalDateTime.now());
        feedDto.setModdate(LocalDateTime.now());

        List<String> hashtagNames = feedDto.getFeedHashtags().stream()
                .map(FeedHashtagDto::getHashtag)
                .toList();

        List<Hashtag> hashtags = hashtagNames.stream()
                                .map(tag -> {
                    Optional<Hashtag> existingTag = hashtagRepository.findByHashtag(tag);
                    return existingTag.orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag();
                        newHashtag.setHashtag(tag);
                        return hashtagRepository.save(newHashtag); // 새 해시태그 저장
                    });
                })
                                .toList();

        // FeedDto의 정보를 Feed 엔티티로 변환하면서 user 정보를 같이 넘긴다.
        Feed feed = feedDto.toEntity(user,hashtags);

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
                    feedFileDto.setFilename(feedFileDto.getFilename());  // 새 파일명 설정

                    feed.getFeedFileList().add(feedFileDto.toEntity(feed));
                }
            });
        }

        Feed savedFeed = feedRepository.save(feed);

        String file = savedFeed.getFeedFileList().isEmpty() ? "" : savedFeed.getFeedFileList().stream().toList().get(0).getFilename();
        kafkaTemplate.send("alarm-topic", "%d:%s:%s:%d"
                .formatted(
                        user.getUserId(),
                        "FEED",
                        file,
                        savedFeed.getFeedId()
                )
        );

        return savedFeed.toDto();
    }

    @Override
    public FeedDto getFeed(Long id) {
        return feedRepository.findById(id).orElseThrow().toDto();
    }

    @Override
    @CacheEvict(value = {"allFeeds", "feedsByUser", "feedsByHashtag"}, allEntries = true)
    public FeedDto updateFeed(Long feedId, FeedDto feedDto, MultipartFile[] uploadFiles, String originFiles, User user) {
        List<FeedFileDto> originFileList = new ArrayList<>();

        // originFiles JSON 문자열을 List<FeedFileDto>로 변환
        try {
            originFileList = new ObjectMapper().readValue(
                    originFiles,
                    new TypeReference<List<FeedFileDto>>() {}
            );
        } catch (IOException ie) {
            System.out.println(ie.getMessage());
        }

        List<FeedFileDto> uFileList = new ArrayList<>();

        // originFiles에서 삭제할 파일만 처리
        if (!originFileList.isEmpty()) {
            originFileList.forEach(feedFileDto -> {
                if (feedFileDto.getFilestatus().equals("D")) {
                    FeedFileDto deleteFeedFileDto = new FeedFileDto();

                    deleteFeedFileDto.setFeedId(feedDto.getFeedId());
                    deleteFeedFileDto.setFeedFileId(feedFileDto.getFeedFileId());
                    deleteFeedFileDto.setFilestatus("D");

                    fileUtils.deleteFile("feed/", feedFileDto.getFilename());
                    uFileList.add(deleteFeedFileDto);
                }
            });
        }

        // 새로 업로드된 파일 처리
        if (uploadFiles != null && uploadFiles.length > 0) {
            Arrays.stream(uploadFiles).forEach(file -> {
                if (!file.getOriginalFilename().isEmpty()) {
                    FeedFileDto postFeedfileDto = fileUtils.parserFileInfo(file, "feed/");
                    postFeedfileDto.setFeedId(feedDto.getFeedId());
                    postFeedfileDto.setFilestatus("I");

                    uFileList.add(postFeedfileDto);
                }
            });
        }

        feedDto.setModdate(LocalDateTime.now());

        Feed feed = feedRepository.findById(feedDto.getFeedId())
                .orElseThrow(() -> new RuntimeException("Feed not exist"));

        feed.setContent(feedDto.getContent());
        feed.setModdate(feedDto.getModdate());

        uFileList.forEach(feedFileDto -> {
            if (feedFileDto.getFilestatus().equals("I")) {
                feed.getFeedFileList().add(feedFileDto.toEntity(feed));
            } else if (feedFileDto.getFilestatus().equals("D")) {
                fileUtils.deleteFile("feed/", feedFileDto.getFilename());
                feedFileRepository.delete(feedFileDto.toEntity(feed));
            }
        });

        Feed updatedFeed = feedRepository.save(feed);
        return updatedFeed.toDto();
    }

    @Override
    @Cacheable(value = "allFeeds")
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

    @Override
    public List<FeedDto> getFeedsByUserId(Long userId) {
        List<FeedDto> feedDtoList = new ArrayList<>();
        List<Feed> feedList = feedRepository.findByUser_UserId(userId);
        for (Feed feed : feedList) {
            feedDtoList.add(convertToDto(feed));
        }
        return feedDtoList;
    }


    @Override
    //@Cacheable(value = "feedsByUser", key = "#userId")
    public Page<FeedDto> getAllFeedsExcludingUser(Long userId, Pageable pageable) {

        // 모든 게시글 가져오기
        Page<Feed> feedList = feedRepository.findByUser_UserIdNot(userId, pageable);

        return feedList.map(this::convertToDto);
    }

    @Override
    public Page<FeedDto> getAllFollowingFeeds(Long userId, Pageable pageable) {

        // 팔로우한 사용자 목록 가져오기
        List<Long> followingIdList = followRepository.findFollowingUserIdsByFollowerUserId(userId);

        // 팔로우한 사용자 게시물 가져오기
        return feedRepository.findByUser_UserIdIn(followingIdList, pageable)
                .map(this::convertToDto);
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
        feedDto.setFollowing(feed.isFollowing());

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
                    return feedFileDto;
                }).toList();
        feedDto.setFeedFileDtoList(feedFileDtoList);

        // 좋아요 개수
        feedDto.setLikeCount(feed.getLikeCount());

        // 댓글 가져오는 기능 구현

        return feedDto;
    }


}
