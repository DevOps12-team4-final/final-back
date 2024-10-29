package com.bit.finalproject.controller;

import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.dto.RoomChatDto;
import com.bit.finalproject.dto.RoomDto;
import com.bit.finalproject.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebsocketController {

    private final RoomService roomService;




    @MessageMapping(value = "/join")
    public void roomJoin(RoomChatDto chat) {
        try{
            System.out.println("chat");
            System.out.println(chat);
            roomService.joinRoom(chat.getRoom_id(), chat.getUser_id());
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }


    @MessageMapping(value = "/enter")
    public void roomEnter(String message){
        try{
          //roomService.();
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

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
            roomService.leaveRoom(chat.getRoom_id(), chat.getUser_id());
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
