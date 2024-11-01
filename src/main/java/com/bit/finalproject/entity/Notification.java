package com.bit.finalproject.entity;

import com.bit.finalproject.dto.NotificationDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String type;

    private String message;

    private Long url;

    private Long senderId;
;
    private LocalDateTime createdAt;



    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAlarmTime; // 알림 생성 시간


    // NotificationDto로 변환하는 메서드 수정
    public NotificationDto toDto() {
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
