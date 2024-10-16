package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.WorkoutDto;
import com.bit.finalproject.dto.WorkoutPlanDto;
import com.bit.finalproject.dto.WorkoutRoutineDto;
import com.bit.finalproject.entity.*;
import com.bit.finalproject.jwt.JwtProvider;
import com.bit.finalproject.repository.UserRepository;
import com.bit.finalproject.repository.WorkoutPlanRepository;
import com.bit.finalproject.repository.WorkoutRepository;
import com.bit.finalproject.repository.WorkoutRoutineRepository;
import com.bit.finalproject.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final WorkoutRepository workoutRepository;
    private final WorkoutRoutineRepository workoutRoutineRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserRepository userRepository;

    @Override
    public List<WorkoutDto> findByCategory(WorkoutDto workoutDto) {
        List<WorkoutDto> workoutDtoList = new ArrayList<>();

        List<Workout> workouts = workoutRepository.findAllByMainCategory(workoutDto.getMainCategory());

        for (Workout workout : workouts) {
            workoutDtoList.add(workout.toDto());
        }
        
        return workoutDtoList;
    }

    @Override
    public List<WorkoutRoutineDto> findByWorkoutId(List<WorkoutDto> workoutDtoList) {
        List<WorkoutRoutineDto> workoutRoutineDtoList = new ArrayList<>();

        for (WorkoutDto workoutDto : workoutDtoList) {
            WorkoutRoutineDto workoutRoutineDto = new WorkoutRoutineDto();

            workoutRoutineDto.setWorkoutId(workoutDto.getWorkoutId());
            workoutRoutineDto.setWorkoutName(workoutDto.getWorkoutName());
            workoutRoutineDto.setMainCategory(workoutDto.getMainCategory());

            List<WorkoutSet> workoutSetList = new ArrayList<>();
            workoutRoutineDto.setWorkoutSetList(workoutSetList); // WorkoutSet 객체 하나 추가해주기

            workoutRoutineDtoList.add(workoutRoutineDto);
        }

        return workoutRoutineDtoList;
    }

    @Override
    public WorkoutPlanDto addWorkoutPlan(List<WorkoutRoutineDto> workoutRoutineDtoList) {
        WorkoutPlanDto workoutPlanDto = new WorkoutPlanDto();
        List<WorkoutRoutine> workoutRoutineList = new ArrayList<>();

        // WorkoutRoutineDto를 엔티티로 변환하여 리스트에 추가
        for (WorkoutRoutineDto workoutRoutineDto : workoutRoutineDtoList) {
            // WorkoutRoutine 엔티티 변환
            Workout workout = workoutRepository.findByWorkoutId(workoutRoutineDto.getWorkoutId()); // workout 찾기
            WorkoutRoutine workoutRoutine = workoutRoutineDto.toEntity(workout);

            workoutRoutineList.add(workoutRoutine); // 루틴 엔티티를 리스트에 추가 (루프 후에 일괄 저장)
        }
        User user = userRepository.findByUserId(workoutPlanDto.getUserId()); // UserRepository에 findByUserId 추가해야함.

        WorkoutPlan workoutPlan = workoutPlanDto.toEntity(user); // WorkoutPlan 엔티티 변환

        workoutPlanDto.setWorkoutRoutineList(workoutRoutineList);  // for문으로 변환한 엔티티 plan객체에 추가

        workoutPlanRepository.save(workoutPlan); // WorkoutPlan 저장

        workoutRoutineRepository.saveAll(workoutRoutineList); // 변환한 routine리스트 한 번에 저장

        return workoutPlanDto;
    }

    @Override
    public WorkoutPlanDto deleteWorkoutPlanById(Long planId) {
        workoutPlanRepository.deleteById(planId);

        WorkoutPlan workoutPlan = workoutPlanRepository.findById(planId).orElseThrow(
                () -> new RuntimeException("workoutPlan not exist") // 플랜 없을 때 표출할 문구 화면단에서 구현하기 ! //
        );

        WorkoutPlanDto workoutPlanDto = workoutPlan.toDto();

        return workoutPlanDto;
    }

}
