package com.bit.finalproject.repository;

import com.bit.finalproject.entity.BookMark;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    boolean existsByFeedAndUser(Feed feed, User user);

    Optional<BookMark> findByFeedAndUser(Feed feed, User user);

    List<BookMark> findByUser_UserId(Long userId);
}
