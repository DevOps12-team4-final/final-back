package com.bit.finalproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
        name = "userSeqGenerator",
        sequenceName = "USER_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userSeqGenerator"
    )
    long user_id;
    String email;
    String password;
    String username;
    String nickname;
    LocalDateTime regdate;
    LocalDateTime moddate;
    LocalDateTime lastlogindate;
    String userstatus;
    String profileimage;
}
