package com.bit.finalproject.service.impl;


import com.bit.finalproject.common.ChatFileUtils;
import com.bit.finalproject.common.FileUtils;
import com.bit.finalproject.dto.RoomChatDto;
import com.bit.finalproject.dto.RoomDto;
import com.bit.finalproject.entity.Room;
import com.bit.finalproject.repository.RoomChatRepository;
import com.bit.finalproject.repository.RoomMemberRepository;
import com.bit.finalproject.repository.RoomRepository;
import com.bit.finalproject.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final RoomMemberRepository roomMemberRepository;
    private final ChatFileUtils fileUtils;
    private final RedisTemplate<String, RoomChatDto> redisTemplate;

    @Override
    public Page<RoomDto> getRooms(String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            // 검색어가 없으면 모든 방 리스트를 페이징하여 반환
            return roomRepository.findAll(pageable).map(Room::toDto);
        } else {
            // 검색어가 있으면 검색어에 맞는 방 리스트를 페이징하여 반환
            return roomRepository.findByTitleDescriptionOrNicknameContaining(searchKeyword, pageable)
                    .map(Room::toDto);
        }
    }

    @Override
    public Page<RoomDto> getFollowRooms(Long userId, Pageable pageable) {
        return null;
    }


    @Override
    public RoomDto createRoom(RoomDto roomDto) {
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

        //room에 들어가 자격이있나?
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return null;
        }
        boolean userExists = room.getMemberList().stream()
                .anyMatch(member -> member.getUser().getUserId().equals(userId));
        if (!userExists) {
            return null;
        }


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

        //chatList 필터링  주인인지 일반인지
        Long masterId =room.getUser().getUserId();
        if (!masterId.equals(userId) && chatList != null) {
            // 일반 사용자일 경우 자신과 주인 간의 채팅만 필터링
            chatList = chatList.stream()
                    .filter(chat -> chat.getUserId().equals(userId) || chat.getUserId().equals(masterId))
                    .toList();
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
        Long userId = roomChatDto.getUserId();
        Long roomId = roomChatDto.getRoomId();
        //kafka에 던지기
        kafkaTemplate.send("chat-topic" ,"%s:chat:%s:%s".formatted( roomId,userId, message));
    }

    @Override
    public void sendFile(RoomChatDto roomChatDto, MultipartFile[] multipartFiles) {
        //파일처리 나중에
        Long userId = roomChatDto.getRoomId();
        Long roomId = roomChatDto.getUserId();
        for (MultipartFile multipartFile : multipartFiles) {
            RoomChatDto curFileMessage = fileUtils.parserFileInfo(multipartFile ,"chatFile/");
            String message = curFileMessage.getMessage();
            String type = curFileMessage.getType();
            kafkaTemplate.send("chat-topic" ,"%s:%s:%s:%s".formatted( roomId,type,userId, message));
        }

    }

}
