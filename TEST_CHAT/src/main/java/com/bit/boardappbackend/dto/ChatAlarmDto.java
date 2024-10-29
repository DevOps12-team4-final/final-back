package com.bit.finalproject.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatAlarmDto {
    private  Long id;
    private  Long user_id;
    private  Long room_id;
    private  int count;
    private  String message;

}
