package com.bit.boardappbackend.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomChatAudioDto {
    private byte[] message;
    private Long user_id;
    private Long room_id;
}
