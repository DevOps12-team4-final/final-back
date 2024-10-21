package com.bit.boardappbackend.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomChatMediaDto {
    private byte[] message;
    private Long userId;
    private Long roomId;
}
