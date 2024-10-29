package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RoomRepository extends JpaRepository<Room, Long> {

    // 방 소유자의 ID로 방 검색
    @Query("SELECT r FROM Room r WHERE r.user.id = :userId")
    Room findByUserId(@Param("userId") Long userId);

    // 방 소유자의 닉네임으로 방 검색
    @Query("SELECT r FROM Room r JOIN r.user u WHERE u.nickname LIKE %:nickname%")
    Page<Room> findByUserNicknameContaining(@Param("nickname") String nickname, Pageable pageable);

    // 방 이름, 설명, 또는 방 소유자의 닉네임으로 방 검색
    @Query("SELECT r FROM Room r JOIN r.user u " +
            "WHERE r.title LIKE %:keyword% OR r.description LIKE %:keyword% OR u.nickname LIKE %:keyword%")
    Page<Room> findByTitleDescriptionOrNicknameContaining(@Param("keyword") String keyword, Pageable pageable);


}
