package com.bit.finalproject.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomChatAudioDto {
    private byte[] message;
    private Long userId;
    private Long roomId;
}
