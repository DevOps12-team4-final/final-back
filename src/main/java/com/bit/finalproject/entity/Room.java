package com.bit.finalproject.entity;


import com.bit.finalproject.dto.RoomDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    private String profileImg; // 일단 주인장 프로필 이미지 챙겨오기

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;  // 방 주인

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
                .profileImg(profileImg)
                .chatDtoList(
                        chatList != null && !chatList.isEmpty()
                                ? chatList.stream().map(RoomChat::toDto).toList()
                                : new ArrayList<>()
                )
                .memberDtoList(
                        memberList != null && !memberList.isEmpty()
                                ? memberList.stream().map(RoomMember::toDto).toList()
                                : new ArrayList<>()
                )
                .build();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
