package com.bit.finalproject.repository;

import com.bit.finalproject.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {


    //방삭제
    void deleteByRoomId(Long roomId);

    //방나가기
    void deleteByRoomIdAndUserUserId(Long roomId, Long userId);

    //유저가 계정삭제
    void deleteByUserUserId(Long userId);

    //방 유저 가져오가
    List<RoomMember> findByRoomId(Long RoomId);


    List<RoomMember> findByRoom_id(Long roomId);
}
