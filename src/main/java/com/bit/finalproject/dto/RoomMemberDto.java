package com.bit.finalproject.dto;


import com.bit.finalproject.entity.Room;
import com.bit.finalproject.entity.RoomMember;
import com.bit.finalproject.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomMemberDto {
    private  Long id;
    private  Long roomId;
    private  Long userId;
    private  String nickname;
    private  String profileImage;


    public RoomMember toEntity(Room room, User user) {
        return  RoomMember.builder()
                .user(user)
                .room(room)
                .build();
    }

}
