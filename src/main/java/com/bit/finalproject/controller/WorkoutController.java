package com.bit.finalproject.controller;

import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.dto.WorkoutDto;
import com.bit.finalproject.dto.WorkoutPlanDto;
import com.bit.finalproject.dto.WorkoutRoutineDto;
import com.bit.finalproject.entity.CustomUserDetails;
import com.bit.finalproject.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor // 생성자 생성 안 해도 됌
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;
    // ------------------- //
    // 운동 목록 가져오는 메소드 //
    // ------------------- //
    @GetMapping("/list")
    public ResponseEntity<?> getWorkoutList(@RequestBody WorkoutDto workoutDto) {
        ResponseDto<List<WorkoutDto>> responseDto = new ResponseDto<>();

        try {
            List<WorkoutDto> workoutDtoList = workoutService.findByCategory(workoutDto);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(workoutDtoList);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("getWorkoutList error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());

            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    // --------------------------------------------- //
    // 선택된 [운동]을 [루틴]으로 [플랜]에 추가(조회)하는 메소드 //
    // --------------------------------------------- //
    @GetMapping("/plan")
    public ResponseEntity<?> addRoutineOnPlan(@RequestBody List<WorkoutDto> selectedWorkoutList,
                                              // CustomUserDetails객체는 현재 로그인된 사용자 정보를 담고있다.
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDto<List<WorkoutRoutineDto>> responseDto = new ResponseDto<>();

        try {
            List<WorkoutRoutineDto> workoutRoutineDtoList = workoutService.findByWorkoutId(selectedWorkoutList);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(workoutRoutineDtoList);

            // 데이터 값 확인 필요
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("addWorkoutOnPlan error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());

            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    // ----------------------------------------- //
    // [오늘 운동 완료하기] 버튼 클릭시 캘린더 박제하는 기능 //
    // ----------------------------------------- //
    @PostMapping("/plan")
    public ResponseEntity<?> addWorkoutPlanOnCalendar(@RequestBody List<WorkoutRoutineDto> workoutRoutineDtoList) {
        ResponseDto<WorkoutPlanDto> responseDto = new ResponseDto<>();

        try {
            WorkoutPlanDto workoutPlanDto = workoutService.addWorkoutPlan(workoutRoutineDtoList);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(workoutPlanDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("addWorkoutPlanOnCalendar error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());

            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
    /**
     *
     * Workout 저장하는 메소드
     * @param workoutDto
     * @return
     */
    @PostMapping("/workout")
    public ResponseEntity<?> setWorkout(@RequestBody WorkoutDto workoutDto) {
        ResponseDto<WorkoutDto> responseDto = new ResponseDto<>();
        try {
            WorkoutDto workout = workoutService.setWorkout(workoutDto);
            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(workout);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("setWorkout error : {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());

            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
    @DeleteMapping("/plan/{plan_id}")
    public ResponseEntity<?> deleteWorkoutPlanOnCalendar(@PathVariable("plan_id") Long planId) {
        ResponseDto<WorkoutPlanDto> responseDto = new ResponseDto<>();

        try {
            WorkoutPlanDto workoutPlanDto = workoutService.deleteWorkoutPlanById(planId);

            responseDto.setStatusCode(HttpStatus.NO_CONTENT.value());
            responseDto.setStatusMessage("no content");
            responseDto.setItem(workoutPlanDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("deleteWorkoutPlanOnCalendar error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());

            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
}
