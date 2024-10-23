package com.bit.finalproject.livestation.controller;

import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.livestation.dto.LiveStationInfoDto;
import com.bit.finalproject.livestation.dto.LiveStationUrlDto;
import com.bit.finalproject.livestation.service.LiveStationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/live")
@RequiredArgsConstructor
@RestController
public class LiveStationRestController {
    private final LiveStationService liveStationService;

    @PostMapping("/create")
    public ResponseEntity<?> createChannelInfo(@RequestBody String name) {
        ResponseDto<String> ResponseDto = new ResponseDto<>();

        String dto = liveStationService.createChannel(name);

        ResponseDto.setItem(dto);
        ResponseDto.setStatusCode(HttpStatus.OK.value());

        return ResponseEntity.ok().body(ResponseDto);
    }

    @GetMapping("/info/{channelId}") //채널 아이디가 있지만 강의중 것을 확인할 때
    public ResponseEntity<?> getChannelInfo(@PathVariable String channelId) {
        ResponseDto<LiveStationInfoDto> ResponseDto = new ResponseDto<>();

        LiveStationInfoDto dto = liveStationService.getChannelInfo(channelId);

        ResponseDto.setItem(dto);
        ResponseDto.setStatusCode(HttpStatus.OK.value());

        return ResponseEntity.ok().body(ResponseDto);
    }

    @GetMapping("/url/{channelId}")
    public ResponseEntity<?> getServiceURL(@PathVariable String channelId) {
        ResponseDto<LiveStationUrlDto> ResponseDto = new ResponseDto<>();

        List<LiveStationUrlDto> dtoList = liveStationService.getServiceURL(channelId, "GENERAL");

        ResponseDto.setItems(dtoList);
        ResponseDto.setStatusCode(HttpStatus.OK.value());

        return ResponseEntity.ok().body(ResponseDto);
    }

    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<?> deleteChannel(@PathVariable String channelId) {
        return liveStationService.deleteChannel(channelId);
    }
}