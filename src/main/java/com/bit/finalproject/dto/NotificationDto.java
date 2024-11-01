package com.bit.finalproject.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationDto {

    private Long id;

    private Long userId;

    private String type;

    private String message;

    private Long url;

    private Long senderId;

    private LocalDateTime createdAt; // 알림 생성 시간

    // NotificationDto로 변환하는 메서드 수정
    public NotificationDto toEntity() {
        return NotificationDto.builder()
                .id(id)
                .userId(userId)
                .type(type)
                .message(message)
                .url(url)
                .senderId(senderId)
                .createdAt(createdAt)
                .build();
    }

}
