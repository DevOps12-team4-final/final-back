package com.bit.boardappbackend.dto;

import com.bit.boardappbackend.entity.Room;
import com.bit.boardappbackend.entity.RoomChat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomChatDto {
    private Long id;
    private String message;
    private String type;

    private LocalDateTime createdAt;

    private Long user_id;
    private Long room_id;

    public RoomChat toEntity(Room room) {
        return RoomChat.builder()
                .id(id)
                .message(message)
                .type(type)
                .createdAt(createdAt)
                .user_id(user_id)
                .room(room)
                .build();
    }
}
