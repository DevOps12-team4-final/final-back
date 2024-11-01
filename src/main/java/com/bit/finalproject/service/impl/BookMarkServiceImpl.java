package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.FeedDto;
import com.bit.finalproject.entity.BookMark;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.repository.BookMarkRepository;
import com.bit.finalproject.repository.FeedRepository;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookMarkServiceImpl implements BookMarkService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;

    @Override
    public void addBookMark(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid FeedId:" + feedId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        // 이미 등록됬는지 확인
        if (bookMarkRepository.existsByFeedAndUser(feed, user)) {
            throw new IllegalArgumentException("User has already marked this feed.");
        }

        // 북마크 추가
        BookMark bookMark = BookMark.builder()
                .feed(feed)
                .user(user)
                .build();
        bookMarkRepository.save(bookMark);
    }

    @Override
    public void removeBookMark(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid FeedId:" + feedId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        BookMark bookMark = bookMarkRepository.findByFeedAndUser(feed, user)
                .orElseThrow(() -> new IllegalArgumentException("BookMark not found."));

        bookMarkRepository.delete(bookMark);
    }

    @Override
    public List<FeedDto> getBookmarks(Long userId) {
        List<BookMark> bookmarks = bookMarkRepository.findByUser_UserId(userId);

        // 북마크에서 Feed 정보를 가져와 FeedDto로 변환
        return bookmarks.stream()
                .map(bookmark -> bookmark.getFeed().toDto()) // Feed의 toDto() 메서드 호출
                .collect(Collectors.toList());
    }


}
