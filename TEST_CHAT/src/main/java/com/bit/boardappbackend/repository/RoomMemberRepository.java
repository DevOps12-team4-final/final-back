package com.bit.boardappbackend.repository;

import com.bit.boardappbackend.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {


    //방삭제
    void deleteByRoomId(Long roomId);

    //방나가기
    void deleteByRoomIdAndUserId(Long roomId, Long userId);

    //유저가 계정삭제
    void deleteByUserId(Long userId);

    //방 유저 가져오가
    List<RoomMember> findByRoomId(Long RoomId);


    //change data들  유저 들어감,나감  방송중,방송안함
    @Modifying
    @Query("UPDATE RoomMember m SET m.isAudio = :audio " +
            "WHERE m.room.id = :roomId AND m.userId != :userId")
    void updateAudio(Long roomId, Long userId,boolean audio);

    @Modifying
    @Query("UPDATE RoomMember m SET m.isVideo = :video " +
            "WHERE m.room.id = :roomId AND m.userId != :userId")
    void updateVideo(Long roomId, Long userId,boolean video);




}
