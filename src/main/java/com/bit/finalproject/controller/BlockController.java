package com.bit.finalproject.controller;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.service.BlockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/blocks")
@RequiredArgsConstructor
@Slf4j
public class BlockController {
    private final BlockService blockService;
    // 사용자 차단
    @PostMapping
    public ResponseEntity<?> blockUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                       @RequestBody UserDto userDto) {
        ResponseDto<UserDto> responseDto = new ResponseDto();
        try{
            Long userId = customUserDetails.getUser().getUserId();
            Long blockedUserId = userDto.getUserId();
            blockService.blockUser(userId, blockedUserId);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("User blocked successfully");
            return ResponseEntity.ok(responseDto);
        } catch(Exception e){
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Failed to block user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
    // 사용자 차단 해제
    @DeleteMapping("/{blockedUserId}")
    public ResponseEntity<?> unblockUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @PathVariable Long blockedUserId) {
        ResponseDto<Void> responseDto = new ResponseDto<>();
        try {
            Long userId = customUserDetails.getUser().getUserId();
            blockService.unblockUser(userId, blockedUserId);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("User unblocked successfully");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Failed to unblock user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}
