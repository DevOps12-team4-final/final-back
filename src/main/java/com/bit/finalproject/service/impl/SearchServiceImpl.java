package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.SearchDto;
import com.bit.finalproject.entity.Feed;
import com.bit.finalproject.entity.QFeed;
import com.bit.finalproject.entity.QUser;
import com.bit.finalproject.entity.User;
import com.bit.finalproject.service.SearchService;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final EntityManager entityManager;

    @Override
    public List<SearchDto> searchByUser(String searchKeyword) {
        JPAQuery<User> query = new JPAQuery<>(entityManager);
        List<User> users = query.from(QUser.user)
                .where(QUser.user.nickname.containsIgnoreCase(searchKeyword))
                .fetch();

        return users.stream()
                .map(user -> SearchDto.builder()
                        .nickname(user.getNickname())
                        .searchCondition("USER")
                        .searchKeyword(searchKeyword)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchDto> searchByFeedContent(String searchKeyword) {
        JPAQuery<Feed> query = new JPAQuery<>(entityManager);
        List<Feed> feeds = query.from(QFeed.feed)
                .where(QFeed.feed.content.containsIgnoreCase(searchKeyword))
                .fetch();

        return feeds.stream()
                .map(feed -> SearchDto.builder()
                        .content(feed.getContent())
                        .searchCondition("FEED")
                        .searchKeyword(searchKeyword)
                        .build())
                .collect(Collectors.toList());
    }
}
