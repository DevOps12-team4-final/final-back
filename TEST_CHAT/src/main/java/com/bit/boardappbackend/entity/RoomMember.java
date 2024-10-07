package com.bit.boardappbackend.entity;


import com.bit.boardappbackend.dto.RoomMemberDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "roomMemberSeqGenerator",
        sequenceName = "ROOM_MEMBER_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomMember {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "roomMemberSeqGenerator"
    )
    private Long id;
    private Long user_id;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;


    public RoomMemberDto toDto() {
        return  RoomMemberDto.builder()
                .id(id)
                .user_id(user_id)
                .room_id(room.getId())
                .build();
    }
}
