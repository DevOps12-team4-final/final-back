package com.bit.finalproject.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
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
