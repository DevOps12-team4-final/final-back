package com.bit.finalproject.controller;

import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<?> follow(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @RequestBody UserDto userDto) {

        ResponseDto<UserDto> responseDto = new ResponseDto<>();

        try{
            Long followerId = customUserDetails.getUser().getUserId();
            Long followingId = userDto.getUserId();

            followService.followUser(followerId, followingId);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Following Successfully");

            return ResponseEntity.ok(responseDto);
        } catch(Exception e){
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @DeleteMapping("/unfollow/{userId}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @PathVariable("userId") Long userId) {

        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        System.out.println(userId);
        try {
            Long followerId = customUserDetails.getUser().getUserId();

            System.out.println(followerId + " " + userId);
            followService.unfollowUser(followerId, userId);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Unfollowing Successfully");

            return ResponseEntity.ok(responseDto);
        } catch(Exception e){
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}
