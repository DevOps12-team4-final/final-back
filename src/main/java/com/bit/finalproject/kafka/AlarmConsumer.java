package com.bit.finalproject.kafka;

import com.bit.finalproject.dto.RoomChatDto;
import com.bit.finalproject.entity.Follow;
import com.bit.finalproject.entity.Notification;
import com.bit.finalproject.repository.FollowRepository;
import com.bit.finalproject.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlarmConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final FollowRepository followRepository;
    private final NotificationRepository notificationRepository;


    @KafkaListener(topics = "alarm-topic", groupId = "alarm_id")
    public void sendAlarm(String message) {
        System.out.println(message);
        String[] parts = message.split(":", 4);
        Long user_id = Long.parseLong(parts[0]);
        String type = parts[1];
        String chatMessage = parts[2]; //이미지 url
        Long url = Long.parseLong(parts[3]);// 경로: roomId ,feedId, profileId


        List<Follow> list = followRepository.findAllByFollower_UserId(user_id);
        for (Follow follow : list) {
             //type 별로 쪼개기
             Long target_id =follow.getFollower().getUserId();
             String nickname =  follow.getFollower().getNickname();
             String profile =  follow.getFollower().getProfileImage();
             String jsonMessage = String.format("{\"message\":\"%s\", " +
                                                "\"type\":\"%s\", " +
                                                "\"nickname\":%s, " +
                                                "\"profile\":%s, " +
                                                "\"url\":%d}",
                                        chatMessage, type, nickname, profile, url);
            String way = String.format("/topic/alarm/%s",target_id);
            messagingTemplate.convertAndSend(way,jsonMessage);
        }

    }

    @KafkaListener(topics = "alarm-topic", groupId = "save_alarm_id")
    public void saveAlarm(String message) {
        System.out.println(message);
        String[] parts = message.split(":", 4);
        Long user_id = Long.parseLong(parts[0]);
        String type = parts[1];
        String chatMessage = parts[2]; //이미지 url
        Long url = Long.parseLong(parts[3]);// 경로: roomId ,feedId, profileId
        //보내야될 팔로워들 id 목록
        List<Follow> list = followRepository.findAllByFollower_UserId(user_id);
        for (Follow follow : list) {
            //type 별로 쪼개기
            Long target_id =follow.getFollower().getUserId();
            String nickname =  follow.getFollower().getNickname();
            String profile =  follow.getFollower().getProfileImage();
            String JsonMessage = String.format("{\"message\":\"%s\", " +
                            "\"type\":\"%s\", " +
                            "\"nickname\":%s, " +
                            "\"profile\":%s, " +
                            "\"url\":%d}",
                    chatMessage, type, nickname, profile, url);
            notificationRepository.save(Notification.builder()
                            .alarmUserId(user_id)
                            .alarmTargetId(target_id)
                            .alarmType(type)
                            .alarmContent(JsonMessage)
                            .createdAlarmTime(LocalDateTime.now())
                            .isRead(false)
                            .build());
        }
    }


    @KafkaListener(topics = "chat-topic", groupId = "alarm_id")
    public void sendChatAlarm(String message) {
        System.out.println(message);
        String[] parts = message.split(":", 4);
        Long roomId = Long.parseLong(parts[0]);
        String type = parts[1];
        Long user_id = Long.parseLong(parts[2]);
        String chatMessage = parts[3];
        String jsonMessage = String.format("{\"message\":\"%s\", \"type\":\"%s\", \"user_id\":%d}", chatMessage, type, user_id);
        String way = String.format("/topic/alarm/%s",user_id);
        messagingTemplate.convertAndSend(way,jsonMessage);
    }

}
