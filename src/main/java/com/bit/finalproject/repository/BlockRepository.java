package com.bit.finalproject.repository;
import com.bit.finalproject.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface BlockRepository extends JpaRepository<Block, Long> {
    boolean existsByUserIdAndBlockedUserId(Long userId, Long blockedUserId);
    void deleteByUserIdAndBlockedUserId(Long userId, Long blockedUserId);
    @Query("SELECT b.blockedUserId FROM Block b WHERE b.userId = :userId")
    List<Long> indBlockedUserIdsByUserId(Long userId);
}