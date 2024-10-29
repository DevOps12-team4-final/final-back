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
    private  Long userId;
    private  Long roomId;
    private  int count;
    private  String message;

}
