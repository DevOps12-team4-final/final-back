package com.bit.finalproject.entity;


import com.bit.finalproject.dto.RoomMemberDto;
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

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;


    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;


    public RoomMemberDto toDto() {
        return  RoomMemberDto.builder()
                .id(id)
                .userId(user.getUserId())
                .profileImage(user.getProfileImage())
                .nickname(user.getNickname())
                .roomId(room.getId())
                .build();
    }
}
