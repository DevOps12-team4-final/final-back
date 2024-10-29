package com.bit.finalproject.service.impl;


import com.bit.finalproject.dto.NotificationDto;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.FeedLike;

import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.FeedCommentRepository;
import com.bit.finalproject.repository.FeedLikeRepository;
import com.bit.finalproject.repository.FeedRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.FeedLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
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
    private  final KafkaTemplate<String, String> kafkaTemplate;

    // 좋아요 추가
    @Override
    @CacheEvict(value = "likeCount", key = "#feedId")
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
        //kafka 로 보내기
        kafkaTemplate.send("alarm-topic", "%d:%s:%s:%d:"
                .formatted(userId,
                        "FEED_LIKE",
                        "feed_like",
                        feedId
                )
        );

    }

    // 좋아요 삭제
    @Override
    @CacheEvict(value = "likeCount", key = "#feedId")
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
    @Cacheable(value = "likeCount", key = "#feedId")
    public int getLikeCount(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid boardId:" + feedId));

        // 좋아요 개수 반환
        return feedLikeRepository.countByFeed(feed);
    }
}
