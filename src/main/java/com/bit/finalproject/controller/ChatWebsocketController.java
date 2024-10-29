package com.bit.finalproject.controller;

import com.bit.finalproject.dto.RoomChatDto;
import com.bit.finalproject.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebsocketController {

    private final RoomService roomService;


    @MessageMapping(value = "/send")
    public void chattingSend(RoomChatDto chat){
        try{
            roomService.sendMessageToKafka(chat);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

    @MessageMapping(value = "/exit")
    public void roomExit(RoomChatDto chat){
        try{
            roomService.leaveRoom(chat.getRoomId(), chat.getUserId());
        }catch(Exception e){
             log.error(e.getMessage());
        }
    }

    @MessageMapping(value = "/setting")
    public void roomSetting(String message){
        try{
            //roomService.changeSettingRoom();
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }



    //audio messsage  추가 작업이 없으므로 바로 보내기
    @MessageMapping(value = "/audio-send")
    @SendTo("/topic/audio")
    public void audioSend(){

    }
}
