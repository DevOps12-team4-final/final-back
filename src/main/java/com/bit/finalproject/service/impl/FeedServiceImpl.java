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
import com.bit.finalproject.repository.FeedHashtagRepository;
import com.bit.finalproject.repository.FeedRepository;
import com.bit.finalproject.repository.HashtagRepository;
import com.bit.finalproject.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<FeedDto> post(FeedDto feedDto, MultipartFile[] uploadFiles, User user, Pageable pageable) {
        feedDto.setRegdate(LocalDateTime.now());
        feedDto.setModdate(LocalDateTime.now());

        // 해시태그 처리
        List<String> hashtagNames = feedDto.getFeedHashtags().stream()
                .map(FeedHashtagDto::getHashtag)
                .collect(Collectors.toList());

        List<Hashtag> hashtags = hashtagNames.stream()
                .map(tag -> {
                    Optional<Hashtag> existingTag = hashtagRepository.findByHashtag(tag);
                    return existingTag.orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag();
                        newHashtag.setHashtag(tag);
                        return hashtagRepository.save(newHashtag); // 새 해시태그 저장
                    });
                })
                .collect(Collectors.toList());

// Feed 저장 (Feed는 Hashtag와 관계 없이 먼저 저장)
        Feed feed = feedDto.toEntity(user, new ArrayList<>());
        Feed savedFeed = feedRepository.save(feed); // Feed 먼저 저장

// FeedHashtag 생성 및 저장
        List<FeedHashtag> feedHashtags = hashtags.stream()
                .map(hashtag -> {
                    FeedHashtag feedHashtag = FeedHashtag.builder()
                            .feed(savedFeed)      // Feed와 Hashtag 연결
                            .hashtag(hashtag)
                            .build();
                    return feedHashtag;
                })
                .collect(Collectors.toList());

// FeedHashtag 저장
        feedHashtagRepository.saveAll(feedHashtags);

// 저장된 Feed에 FeedHashtag 리스트를 추가
        savedFeed.setFeedHashtags(feedHashtags); // 새로운 변수에 할당하지 않음, 객체의 상태를 변경

// 파일이 업로드 되었다면 파일 처리 로직
        if (uploadFiles != null && uploadFiles.length > 0) {
            Arrays.stream(uploadFiles).forEach(multipartFile -> {
                if (multipartFile.getOriginalFilename() != null &&
                        !multipartFile.getOriginalFilename().equalsIgnoreCase("")) {
                    FeedFileDto feedFileDto = fileUtils.parserFileInfo(multipartFile, "feed/");
                    savedFeed.getFeedFileList().add(feedFileDto.toEntity(savedFeed));
                }
            });
        }

// 최종적으로 Feed와 연관된 파일 및 해시태그가 추가된 상태로 다시 저장
        feedRepository.save(savedFeed);

        return feedRepository.findAll(pageable).map(Feed::toDto);
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

    @Override
    public Page<FeedDto> getAllFeedsExcludingUser(Long userId, Pageable pageable) {

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
        feedDto.setFeedHashtags(feed.toDto().getFeedHashtags());



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
