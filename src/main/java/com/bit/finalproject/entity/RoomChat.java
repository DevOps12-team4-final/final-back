package com.bit.finalproject.entity;

import com.bit.finalproject.dto.RoomChatDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
        name = "roomChatSeqGenerator",
        sequenceName = "ROOM_CHAT_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomChat {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,



            generator = "roomChatSeqGenerator"
    )
    private Long id;
    private String type;
    private String message;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;


    public RoomChatDto toDto(){
        return RoomChatDto.builder()
                .id(id)
                .message(message)
                .createdAt(createdAt)
                .userId(userId)
                .type(type)
                .roomId(room.getId())
                .build();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
