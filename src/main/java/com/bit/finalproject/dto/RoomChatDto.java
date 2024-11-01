package com.bit.finalproject.dto;

import com.bit.finalproject.entity.Room;
import com.bit.finalproject.entity.RoomChat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Long userId;
    private Long roomId;

    public RoomChat toEntity(Room room) {
        return RoomChat.builder()
                .id(id)
                .message(message)
                .type(type)
                .createdAt(createdAt)
                .userId(userId)
                .room(room)
                .build();
    }
}
