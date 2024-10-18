package com.bit.boardappbackend.dto;


import com.bit.boardappbackend.entity.Room;
import com.bit.boardappbackend.entity.RoomMember;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomMemberDto {
    private  Long id;
    private  Long room_id;
    private  Long user_id;

    public RoomMember toEntity(Room room) {
        return  RoomMember.builder()
                .user_id(user_id)
                .room(room)
                .build();
    }

}
