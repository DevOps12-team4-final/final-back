package com.bit.boardappbackend.kafka.consumer;


import com.bit.boardappbackend.dto.RoomChatDto;
import com.bit.boardappbackend.dto.RoomChatMediaDto;
import com.bit.boardappbackend.entity.RoomChat;
import com.bit.boardappbackend.repository.ChatAlarmRepository;
import com.bit.boardappbackend.repository.RoomChatRepository;
import com.bit.boardappbackend.repository.RoomMemberRepository;
import com.bit.boardappbackend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatMediaConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomRepository roomRepository;
    private final RoomChatRepository roomChatRepository;
    private  final ChatAlarmRepository chatAlarmRepository;
    private  final RoomMemberRepository roomMemberRepository;
    private final RedisTemplate<String, RoomChatDto> redisTemplate;

    //audio 알람종합
    @KafkaListener(topics = "audio-topic", groupId = "alarm_id")
    public void memberAdjust(String message){
        System.out.println("AUDIO ALARM: " + message);
        String[] parts = message.split(":", 3);
        Long roomId = Long.parseLong(parts[0]);
        String type = parts[1];
        Long user_id = Long.parseLong(parts[2]);

        String jsonMessage = String.format("\"type\":\"%s\", \"user_id\":%d}",type, user_id);
        String way = String.format("/topic/room_alarm/%s",roomId);
        System.out.println("TRY SEND AUDIO STATUS: " + way);
        messagingTemplate.convertAndSend(way,jsonMessage);



    }

    //DB 처리하기
    @KafkaListener(topics = "audio-topic", groupId = "save_id")
    public void saveRoom(String message) {
        System.out.println("AUDIO ALARM: " + message);
        String[] parts = message.split(":", 3);
        Long roomId = Long.parseLong(parts[0]);
        String type = parts[1];
        Long user_id = Long.parseLong(parts[2]);

        if(type.equals("join_audio")){
            roomMemberRepository.updateAudio(roomId,user_id,true);
        }
        if(type.equals("exit_audio")){
            roomMemberRepository.updateAudio(roomId,user_id,false);
        }
        if(type.equals("start_video")){
            roomMemberRepository.updateVideo(roomId,user_id,true);
        }
        if(type.equals("end_video")){
            roomMemberRepository.updateVideo(roomId,user_id,false);
        }

    }



}
