package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.SearchDto;
import com.bit.finalproject.entity.Uesr;
import com.bit.finalproject.entity.QUesr;
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
    public List<?> searchByUesr(String searchKeyword) {
        JPAQuery<Uesr> query = new JPAQuery<>(entityManager);
        List<Uesr> users = query.from(QUesr.uesr)
                .where(QUesr.uesr.nickname.containsIgnoreCase(searchKeyword))
                .fetch();

        return users.stream()
                .map(user -> SearchDto.builder()
                        .nickname(user.getNickname())
                        .searchCondition("USER")
                        .searchKeyword(searchKeyword)
                        .build())
                .collect(Collectors.toList());
    }
}
