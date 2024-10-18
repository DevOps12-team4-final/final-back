package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    // 해시태그 문자열로 해시태그를 검색하는 메서드
    Optional<Hashtag> findByHashtag(String hashtag);
}
