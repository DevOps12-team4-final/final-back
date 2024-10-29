package com.bit.finalproject.controller;


import com.bit.finalproject.dto.*;
import com.bit.finalproject.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<?> getRoomList(
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ResponseDto<RoomDto> responseDto = new ResponseDto<>();
        try {
            // Pageable 객체 생성
            Pageable pageable = PageRequest.of(page, size);

            // 검색어를 기반으로 방 목록 가져오기
            Page<RoomDto> roomDtoPage = roomService.getRooms(searchKeyword, pageable);

            // 응답 객체 설정
            responseDto.setPageItems(roomDtoPage);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("get Rooms");
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> roomCreate(
            RoomDto roomDto
    ){
        ResponseDto<RoomDto> responseDto = new ResponseDto<>();
        try{
            RoomDto createRoomDto = roomService.createRoom(roomDto);
            responseDto.setItem(createRoomDto);
            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");
            return ResponseEntity.ok().body(responseDto);
        }catch(Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }


    //Alart등 DB 생각 좀더 해보고
    @GetMapping("/join/{room_id}")
    public ResponseEntity<?> joinRoom(@PathVariable Long room_id,
                                       @RequestParam("user_id") Long user_id
    )
    {
        ResponseDto<RoomDto> responseDto = new ResponseDto<>();
        try{
            RoomDto roomDto = roomService.joinRoom(room_id,user_id);
            responseDto.setItem(roomDto);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("join room");
            return ResponseEntity.ok().body(responseDto);
        }catch(Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }


    @PostMapping("/sendFile/{room_id}")
    public ResponseEntity<?> sendFile(
                         RoomChatDto roomChat,
                         MultipartFile[] uploadFiles
    )
    {
        ResponseDto<RoomDto> responseDto = new ResponseDto<>();
        try{
            roomService.sendFile(roomChat, uploadFiles);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("upload files");
            return ResponseEntity.ok().body(responseDto);
        }catch(Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }



    //Alart등 DB 생각 좀더 해보고
    @GetMapping("/enter/{room_id}")
    public ResponseEntity<?> enterRoom(@PathVariable Long room_id,
                          @RequestParam("user_id") Long user_id,
                          @RequestParam(value ="last_chat", required = false) RoomChatDto last_chat
    )
    {
        ResponseDto<RoomChatDto> responseDto = new ResponseDto<>();
        try{
            List<RoomChatDto> roomChatDtoList = roomService.enterRoom(room_id,user_id,last_chat);
            responseDto.setItems(roomChatDtoList);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("entered room");
            return ResponseEntity.ok().body(responseDto);
        }catch(Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping("/alarm")
    public ResponseEntity<?> getChatAlarmList(){
        ResponseDto<RoomDto> responseDto = new ResponseDto<>();
        try{
            //연결 여부 확인후 들어가기
            return null;
        }catch(Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }


}
