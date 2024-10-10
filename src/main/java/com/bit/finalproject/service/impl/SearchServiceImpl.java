package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.SearchDto;
import com.bit.finalproject.entity.Member;
import com.bit.finalproject.entity.QMember;
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
    public List<?> searchByMember(String searchKeyword) {
        JPAQuery<Member> query = new JPAQuery<>(entityManager);
        List<Member> members = query.from(QMember.member)
                .where(QMember.member.nickname.containsIgnoreCase(searchKeyword))
                .fetch();

        return members.stream()
                .map(member -> SearchDto.builder()
                        .nickname(member.getNickname())
                        .searchCondition("MEMBER")
                        .searchKeyword(searchKeyword)
                        .build())
                .collect(Collectors.toList());
    }
}
