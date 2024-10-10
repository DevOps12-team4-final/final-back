package com.bit.finalproject.repository;

import java.util.List;
import com.bit.finalproject.entity.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
    List<FeedComment> findByMember_UserIdAndIsdeleteNot(Long userId, String isdelete);  // 수정된 필드 이름
}
