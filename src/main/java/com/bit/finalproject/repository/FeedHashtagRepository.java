package com.bit.finalproject.repository;

import com.bit.finalproject.entity.FeedHashtag;
import com.bit.finalproject.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FeedHashtagRepository extends JpaRepository<FeedHashtag, Long> {

    // 해시태그명을 통해 관련된 게시글 찾기 (쿼리 없이 메서드 네이밍 규칙 활용)
    Optional<Hashtag> findByHashtag_Hashtag(String hashtag);

    List<FeedHashtag> findByHashtag(Hashtag hashtag);
}
