package com.bit.finalproject.entity;

import com.bit.finalproject.dto.RoomChatDto;
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


    private LocalDateTime createdAt;

    private Long user_id;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;


    public RoomChatDto toDto(){
        return RoomChatDto.builder()
                .id(id)
                .message(message)
                .createdAt(createdAt)
                .user_id(user_id)
                .room_id(room.getId())
                .build();
    }


}
