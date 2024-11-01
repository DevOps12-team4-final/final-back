package com.bit.finalproject.kafka;


import com.bit.finalproject.dto.RoomChatDto;
import com.bit.finalproject.entity.*;
import com.bit.finalproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomRepository roomRepository;
    private final RoomChatRepository roomChatRepository;
    private final RoomMemberRepository roomMemberRepository;
    private  final NotificationRepository notificationRepository;
    private  final FollowRepository followRepository;

    private final RedisTemplate<String, RoomChatDto> redisTemplate;
    private final UserRepository userRepository;


    //socket으로 보내기
    @KafkaListener(topics = "chat-topic", groupId = "chat_id")
    public void sendMessage(String message) {
        String[] parts = message.split(":", 4);
        Long roomId = Long.parseLong(parts[0]);
        String type = parts[1];
        Long user_id = Long.parseLong(parts[2]);
        String chatMessage = parts[3];
        if (!type.equals("join")) {
            String jsonMessage = String.format("{\"message\":\"%s\", \"type\":\"%s\", \"userId\":%d,\"createdAt\":\"%s\" }", chatMessage, type, user_id, LocalDateTime.now());
            String way = String.format("/topic/chat/%s", roomId);
            System.out.println("TRY SEND MESSAGE ROOM_ID: " + way);
            messagingTemplate.convertAndSend(way, jsonMessage);
        }
    }

    //채팅 저장용 DB로 보내기
    @KafkaListener(topics = "chat-topic", groupId = "chat_save_id")
    //@CachePut(value = "chat", key = "#roomId")
    public RoomChat saveMessage(String message) {

        //
        System.out.println("CHAT_SOCKET_CONSUMER: " + message);
        // 메시지를 파싱하여 필요한 정보를 추출
        String[] parts = message.split(":", 4);
        Long roomId = Long.parseLong(parts[0]);  // roomId를 추출합니다.
        String type = parts[1];
        Long user_id = Long.parseLong(parts[2]);
        String chatMessage = parts[3];

        // 메세지 타입에 따라 다르게 처리
        if (!type.equals("join")) {
            RoomChat roomChat = roomChatRepository.save(RoomChat.builder()
                    .type(type)
                    .message(chatMessage)
                    .userId(user_id)
                    .room(roomRepository.findById(roomId).orElse(null))
                    .build()
            );
           ListOperations<String, RoomChatDto> listOps = redisTemplate.opsForList();
           listOps.rightPush("chat:"+roomId,roomChat.toDto());
            return roomChat;  // 반환된 객체가 자동으로 캐시에 저장됩니다.
        }
        // 설정이나 알람이면 null을 반환
        return null;
    }

    //alarm용 DB로 보내기
    @KafkaListener(topics = "chat-topic", groupId = "chat_save_alarm_id")
    public void makeAlarm(String message) {
        String[] parts = message.split(":", 4);
        Long roomId = Long.parseLong(parts[0]);
        String type = parts[1];
        Long user_id = Long.parseLong(parts[2]);
        String chatMessage = parts[3];

        //owner 인지 따라서 처리
        User master = roomRepository.findById(roomId).orElseThrow().getUser();
        Long masterId = master.getUserId();

        if(user_id.equals(masterId)) {
            List<Follow> list = followRepository.findAllByFollowing_UserId(user_id);
            for (Follow follow : list) {
                Long target_id = follow.getFollower().getUserId();
                String nickname = follow.getFollower().getNickname();
                String profile = follow.getFollower().getProfileImage();
                String JsonMessage = String.format("{\"message\":\"%s\", " +
                                "\"type\":\"%s\", " +
                                "\"nickname\":\"%s\", " +
                                "\"profile\":\"%s\", " +
                                "\"url\":%d}",
                        chatMessage, type, nickname, profile, roomId);
                notificationRepository.save(Notification.builder()
                                .userId(target_id)
                                .senderId(user_id)
                                .url(roomId)
                                .type(type)
                                .createdAt(LocalDateTime.now())
                                .message(JsonMessage)
                                .build()
                );
            }
        }

        else{
            String nickname = master.getNickname();
            String profile = master.getProfileImage();
            String JsonMessage = String.format("{\"message\":\"%s\", " +
                            "\"type\":\"%s\", " +
                            "\"nickname\":\"%s\", " +
                            "\"profile\":\"%s\", " +
                            "\"url\":%d}",
                    chatMessage, type, nickname, profile, roomId);
            notificationRepository.save(Notification.builder()
                    .userId(masterId)
                    .senderId(user_id)
                    .url(roomId)
                    .type(type)
                    .createdAt(LocalDateTime.now())
                    .message(JsonMessage)
                    .build());
        }

    }

    //alarm socket으로 보내기
    @KafkaListener(topics = "chat-topic", groupId = "chat_alarm_id")
    public void sendChatAlarm(String message) {
        System.out.println(message);
        String[] parts = message.split(":", 4);
        Long roomId = Long.parseLong(parts[0]);
        String type = parts[1];
        Long user_id = Long.parseLong(parts[2]);
        User user = userRepository.findById(user_id).orElseThrow();
        String nickname =  user.getNickname();
        String profile = user.getProfileImage();
        String chatMessage = parts[3];
        String jsonMessage = String.format("{\"message\":\"%s\", " +
                        "\"type\":\"%s\", " +
                        "\"nickname\":\"%s\", " +
                        "\"profile\":\"%s\", " +
                        "\"url\":%d}",
                chatMessage, type, nickname, profile, roomId);

        //어디로 보낼까요
        //맴버들에게
        List<RoomMember> memberList = roomMemberRepository.findByRoom_id(roomId);
        for (RoomMember roomMember : memberList) {
            String way = String.format("/topic/alarm/%s",roomMember.getUser().getUserId());
            messagingTemplate.convertAndSend(way,jsonMessage);
        }


    }

}
