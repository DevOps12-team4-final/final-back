package com.bit.finalproject.entity;


import com.bit.finalproject.dto.RoomChatDto;
import com.bit.finalproject.dto.RoomDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(
        name = "roomSeqGenerator",
        sequenceName = "ROOM_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "roomSeqGenerator"
    )
    private  Long id;
    private  String title;
    private  String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RoomChat> chatList;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RoomMember> memberList;


     //@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
     //@JsonManagedReference
     // private List<BoardFile> boardFileList;


    public RoomDto toDto() {
        return RoomDto.builder()
                .id(this.id)
                .title(this.title)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .description(description)
                .chatDtoList(
                        chatList != null && !chatList.isEmpty()
                                ? chatList.stream().map(RoomChat::toDto).toList()
                                : new ArrayList<>()
                )
                .build();
    }













}
