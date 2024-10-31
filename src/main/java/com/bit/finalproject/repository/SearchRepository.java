package com.bit.finalproject.repository;

import com.bit.finalproject.entity.User;

import java.util.List;

public interface SearchRepository{

    // 계정 검색
    List<User> searchByUser(String keyword);

//    // 태그 검색
//    List<Tag> searchByTag(String keyword);
//
//    // 채팅방 검색
//    List<Chatroom> searchByChatroom(String keyword);
//
//    // 운동 검색
//    List<Exercise> searchByExercise(String keyword);
}
