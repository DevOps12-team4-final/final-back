package com.bit.finalproject.service.impl;

import com.bit.finalproject.dto.WorkoutDto;
import com.bit.finalproject.dto.WorkoutPlanDto;
import com.bit.finalproject.dto.WorkoutRoutineDto;
import com.bit.finalproject.entity.WorkoutRoutine;
import com.bit.finalproject.entity.WorkoutSet;
import com.bit.finalproject.jwt.JwtProvider;
import com.bit.finalproject.repository.WorkoutRepository;
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

    @Override
    public List<WorkoutDto> findByCategory(WorkoutDto workoutDto) {

        List<WorkoutDto> userSelectedList = workoutRepository.findByCategory(workoutDto.getMainCategory(), workoutDto.getSubCategory(), workoutDto.getFavoriteWorkout());

        if(workoutDto.getSubCategory() == null) { // 첫 화면에 즐겨찾기 조회 되도록 //
            userSelectedList = workoutRepository.findByCategory(workoutDto.getMainCategory(), workoutDto.getSubCategory(), 1);
        }

        return userSelectedList;
    }

    @Override
    public List<WorkoutRoutineDto> findByWorkoutId(List<WorkoutDto> workoutDtoList) {
        List<WorkoutRoutineDto> workoutRoutineDtoList = new ArrayList<>();

        for (WorkoutDto workoutDto : workoutDtoList) {
            WorkoutRoutineDto workoutRoutineDto = new WorkoutRoutineDto();
            workoutRoutineDto.setWorkoutId(workoutRepository.findByWorkoutId(workoutDto.getWorkoutId()).getWorkoutId());
            workoutRoutineDto.setWorkoutName(workoutRepository.findByWorkoutId(workoutDto.getWorkoutId()).getWorkoutName());
            workoutRoutineDto.setMainCategory(workoutRepository.findByWorkoutId(workoutDto.getWorkoutId()).getMainCategory());
            workoutRoutineDto.getWorkoutSetList().add(new WorkoutSet()); // 이거 setWorkoutSetList 해야하나 ?

            workoutRoutineDtoList.add(workoutRoutineDto);
        }

        return workoutRoutineDtoList;
    }

    @Override
    public WorkoutPlanDto addWorkoutPlan(List<WorkoutRoutineDto> workoutRoutineDtoList) {
        // set루틴리스트 할건데 매개변수가 루틴Dto리스트라 엔터티로 변환 해줘서 담아야함.
        // 하나씩 변환하고 루틴리스트에 add해서 -> set루틴리스트 하기
        WorkoutPlanDto workoutPlanDto = new WorkoutPlanDto();
        List<WorkoutRoutine> workoutRoutineList = new ArrayList<>();

        for (WorkoutRoutineDto workoutRoutineDto : workoutRoutineDtoList) {
            workoutRepository.saveWorkoutRoutine(workoutRoutineDto); // 루틴 DB에 저장 //
            workoutRoutineList.add(workoutRoutineDto.toEntity(workoutRepository.findByWorkoutId(workoutRoutineDto.getWorkoutId()).toEntity())); // 값 찍어보기
        }
        workoutPlanDto.setWorkoutRoutineList(workoutRoutineList);
        workoutRepository.saveWorkoutPlan(workoutPlanDto); // 플랜 DB에 저장 //

        return workoutPlanDto;
    }
}
