package com.bit.finalproject.controller;

import com.bit.finalproject.dto.NotificationDto;
import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * NotificationController는 알림 관련 기능을 제공하는 REST API를 관리하는 컨트롤러입니다.
 * 사용자에게 알림을 조회하고, 읽음 상태로 변경하는 등의 기능을 제공합니다.
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * NotificationService를 생성자 주입 방식으로 주입받아 초기화합니다.
     *
     * @param notificationService 알림 관련 비즈니스 로직을 처리하는 서비스
     */
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 특정 사용자에게 전송된 알림 목록을 가져오는 API입니다.
     *
     * @param receiverId 알림을 받을 사용자의 ID
     * @return 사용자의 알림 목록을 포함한 ResponseDto
     */
    @GetMapping("/user/{receiverId}")
    public ResponseDto<List<NotificationDto>> getNotificationsForUser(@PathVariable Long receiverId) {
        ResponseDto<List<NotificationDto>> response = new ResponseDto<>();
        try {
            List<NotificationDto> notifications = notificationService.getNotificationsForUser(receiverId);
            response.setItem(notifications);
            response.setStatusCode(200);
            response.setStatusMessage("알림 목록을 성공적으로 불러왔습니다.");
        } catch (Exception e) {
            response.setItem(null);
            response.setStatusCode(500);
            response.setStatusMessage("알림 목록을 불러오는 데 실패했습니다.");
        }
        return response;
    }


    @DeleteMapping
    public ResponseDto<Void> markNotificationAsRead(Long userId,
                                                    Long url,
                                                    String type ) {
        ResponseDto<Void> response = new ResponseDto<>();
        try {
            notificationService.markNotificationAsRead(userId,url,type);
            response.setStatusCode(200);
            response.setStatusMessage("알림이 읽음 처리되었습니다.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setStatusMessage("알림 읽음 처리에 실패했습니다.");
        }
        return response;
    }
}
