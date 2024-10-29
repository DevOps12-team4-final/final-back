package com.bit.finalproject.service.impl;


import com.bit.finalproject.common.FileUtils;
import com.bit.finalproject.dto.RoomChatDto;
import com.bit.finalproject.dto.RoomDto;
import com.bit.finalproject.entity.Room;
import com.bit.finalproject.entity.RoomChat;
import com.bit.finalproject.repository.RoomChatRepository;
import com.bit.finalproject.repository.RoomRepository;
import com.bit.finalproject.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private  final RoomRepository roomRepository;
    private  final KafkaTemplate<String, String> kafkaTemplate;
    private final RoomChatRepository roomChatRepository;
    private final FileUtils fileUtils;
    private final RedisTemplate<String, RoomChatDto> redisTemplate;

    @Override
    public List<RoomDto> getAllRooms() {
        //방 목록
        return roomRepository.findAll().stream().map(Room::toDto).toList();
    }

    @Override
    public RoomDto createRoom(RoomDto roomDto) {
        //메세지 제작
        return roomRepository.save(roomDto.toEntity()).toDto();
    }

    @Override
    public RoomDto changeSettingRoom(RoomDto roomDto) {
        //메세지 제작

        //kafka에 뿌리고 각 Consumer에서 처리하게
        //kafkaTemplate.send()
        return null;
    }




    @Override
    //@Cacheable(value = "chat", key = "#roomId")
    public List<RoomChatDto> enterRoom(long roomId, long userId, RoomChatDto LastChat) {


        ListOperations<String, RoomChatDto> listOps = redisTemplate.opsForList();
        List<RoomChatDto> chatList =null;
        try {
            String type = redisTemplate.type("chat:" + roomId).toString();
            System.out.println(type);
            if(type.equals("STRING")){
                redisTemplate.delete("chat:" + roomId);
            }
            chatList = listOps.range("chat:" + roomId, 0, -1);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        //없거나 조건 불만족
        if(chatList == null || chatList.isEmpty()) {
            System.out.println("No data in room current");
            //chatList =roomChatRepository.findByRoom_id(roomId).stream().map(RoomChat::toDto).toList();
            //listOps.rightPushAll("chat:" + roomId, chatList);
        }

        return chatList;
    }

    @Override
    public RoomDto joinRoom(long roomId, long userId) {
        //참여조건 확인하고 향후 구현
        boolean flag = true;
        //메세지 제작
        if (flag) {
            System.out.println("TEST JOIN");
            String message = "%s 님이 참여 했습니다.".formatted(userId);
            kafkaTemplate.send("chat-topic" ,"%s:join:%s:%s".formatted(roomId, userId, message));
            //return roomRepository.findById(roomId).orElseThrow().toDto();
        }
        return null;
    }

    @Override
    public void leaveRoom(long roomId, long userId) {
        //메세지 제작
        String message = "%s 님이 나가셧습니다.".formatted(userId);
        //kafka에 던지기
        kafkaTemplate.send("cha-topic" ,"%s:exit:%s:%s".formatted(roomId, userId, message));
    }



    @Override
    public void sendMessageToKafka(RoomChatDto roomChatDto) {
        //메세지 제작
        String message = roomChatDto.getMessage();
        Long userId = roomChatDto.getRoom_id();
        Long roomId = roomChatDto.getUser_id();
        //kafka에 던지기
        kafkaTemplate.send("chat-topic" ,"%s:chat:%s:%s".formatted( roomId,userId, message));
    }

    @Override
    public void sendFile(RoomChatDto roomChatDto, MultipartFile[] multipartFiles) {
        //파일처리 나중에
        Long userId = roomChatDto.getRoom_id();
        Long roomId = roomChatDto.getUser_id();
        for (MultipartFile multipartFile : multipartFiles) {
            RoomChatDto curFileMessage = fileUtils.parserFileInfo(multipartFile ,"chatFile");
            String message = curFileMessage.getMessage();
            String type = curFileMessage.getType();
            kafkaTemplate.send("chat-topic" ,"%s:%s:%s:%s".formatted( roomId,type,userId, message));
        }

    }

}
