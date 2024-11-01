package com.bit.finalproject.dto;


import com.bit.finalproject.entity.Room;
import com.bit.finalproject.entity.RoomMember;
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
