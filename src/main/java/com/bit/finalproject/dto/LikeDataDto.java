package com.bit.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class LikeDataDto {
    private Long likeCount;
    private List<UserDto> likedUsers;
}
