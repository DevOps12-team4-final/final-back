package com.bit.finalproject.service.impl;

import com.bit.finalproject.entity.Block;
import com.bit.finalproject.repository.BlockRepository;
import com.bit.finalproject.service.BlockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;

    @Override
    public void blockUser(Long userId, Long blockedUserId) {
        // 차단 관계가 없을 때만 저장
        if (!blockRepository.existsByUserIdAndBlockedUserId(userId, blockedUserId)) {
            Block block = new Block();
            block.setUserId(userId);
            block.setBlockedUserId(blockedUserId);
            blockRepository.save(block);
        }
    }

    @Override
    @Transactional
    public void unblockUser(Long userId, Long blockedUserId) {
        blockRepository.deleteByUserIdAndBlockedUserId(userId, blockedUserId);
    }

    @Override
    public List<Long> getBlockedUserIds(Long userId) {
        return blockRepository.indBlockedUserIdsByUserId(userId);
    }
}
