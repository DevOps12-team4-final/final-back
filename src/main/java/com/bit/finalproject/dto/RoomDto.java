package com.bit.finalproject.dto;


import com.bit.finalproject.entity.Room;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomDto {
    private  Long id;
    private  String title;
    private  String description;
    private  String profileImg;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<RoomChatDto> chatDtoList;
    private List<RoomMemberDto> memberDtoList;

    public Room toEntity(){
        return Room.builder()
                .id(id)
                .title(title)
                .description(description)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .profileImg(profileImg)
                .chatList(new ArrayList<>())
                .build();
    }
}
