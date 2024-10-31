package com.bit.finalproject.service.impl;


import com.bit.finalproject.dto.NotificationDto;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedLike;

import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.FeedLikeRepository;
import com.bit.finalproject.repository.FeedRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.FeedLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedLikeServiceImpl implements FeedLikeService {

    private final FeedLikeRepository feedLikeRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    @Autowired
    private NotificationServiceImpl notificationService;

    // 좋아요 추가
    @Override
    public void addLike(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid feedId:" + feedId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        // 이미 좋아요를 눌렀는지 확인
        if (feedLikeRepository.existsByFeedAndUser(feed, user)) {
            throw new IllegalArgumentException("User has already liked this post.");
        }

        // 좋아요 추가
        FeedLike feedLike = FeedLike.builder()
                .feed(feed)
                .user(user)
                .build();
        feedLikeRepository.save(feedLike);

        // 알림 생성 로직 추가
        NotificationDto notificationDto = null;
        try {
            notificationDto = new NotificationDto(
                    null,
                    feed.getUser().getUserId(),  // 게시글 작성자에게 알림
                    "게시글에 좋아요가 눌렸습니다.",
                    feed.getFeedId(),  // 게시글 ID
                    "FEED_LIKE",
                    LocalDateTime.now(),
                    false
            );

            notificationService.createNotification(notificationDto);
        } catch (Exception e) {
            log.error("알림 생성 중 오류 발생: {}", e.getMessage());
        }

        // 로그 기록
        log.info("feed ID: {}", feed.getFeedId());
        log.info("feed 작성자 ID: {}", feed.getUser().getUserId());
        if (notificationDto != null) {
            log.info("Alarm Content: {}", notificationDto.getAlarmContent());
            log.info("Alarm Target ID: {}", notificationDto.getAlarmTargetId());
            log.info("Alarm Type: {}", notificationDto.getAlarmType());
            log.info("Created Alarm Time: {}", notificationDto.getCreatedAlarmTime());
            log.info("Is Read: {}", notificationDto.isRead());
        }
    }

    // 좋아요 삭제
    @Override
    public void removeLike(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid boardId:" + feedId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        // 좋아요 찾기
        FeedLike feedLike = feedLikeRepository.findByFeedAndUser(feed, user)
                .orElseThrow(() -> new IllegalArgumentException("Like not found."));

        // 좋아요 삭제
        feedLikeRepository.delete(feedLike);
    }

    // 게시글의 좋아요 총 개수 가져오기
    @Override
    public int getLikeCount(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid boardId:" + feedId));

        // 좋아요 개수 반환
        return feedLikeRepository.countByFeed(feed);
    }
}
