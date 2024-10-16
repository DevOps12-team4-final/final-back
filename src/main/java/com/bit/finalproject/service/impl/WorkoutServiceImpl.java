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

        List<WorkoutDto> userSelectedList = workoutRepository.findByMainCategory(workoutDto);

        if(workoutDto.getSubCategory() == null) { // 첫 화면에 즐겨찾기 조회 되도록 //
            userSelectedList = workoutRepository.findByMainCategory(workoutDto);
        }

        return userSelectedList;
    }

    @Override
    public List<WorkoutRoutineDto> findByWorkoutId(List<WorkoutDto> workoutDtoList) {
        List<WorkoutRoutineDto> workoutRoutineDtoList = new ArrayList<>();

        for (WorkoutDto workoutDto : workoutDtoList) {
            WorkoutRoutineDto workoutRoutineDto = new WorkoutRoutineDto();
//            workoutRoutineDto.setWorkoutId(workoutRepository.findByWorkoutId(workoutDto.getWorkoutId()).getWorkoutId());
//            workoutRoutineDto.setWorkoutName(workoutRepository.findByWorkoutId(workoutDto.getWorkoutId()).getWorkoutName());
//            workoutRoutineDto.setMainCategory(workoutRepository.findByWorkoutId(workoutDto.getWorkoutId()).getMainCategory());
//            workoutRoutineDto.getWorkoutSetList().add(new WorkoutSet()); // 이거 setWorkoutSetList 해야하나 ?

            workoutRoutineDto.setWorkoutId(workoutDto.getWorkoutId());
            workoutRoutineDto.setWorkoutName(workoutDto.getWorkoutName());
            workoutRoutineDto.setMainCategory(workoutDto.getMainCategory());

            List<WorkoutSet> workoutSetList = new ArrayList<>();
            workoutRoutineDto.setWorkoutSetList(workoutSetList); // WorkoutSet 객체 하나 추가해주기

            workoutRoutineDtoList.add(workoutRoutineDto);
        }

        return workoutRoutineDtoList;
    }

//    @Override
//    public WorkoutPlanDto addWorkoutPlan(List<WorkoutRoutineDto> workoutRoutineDtoList) {
//        // set루틴리스트 할건데 매개변수가 루틴Dto리스트라 엔터티로 변환 해줘서 담아야함.
//        // 하나씩 변환하고 루틴리스트에 add해서 -> set루틴리스트 하기
//        WorkoutPlanDto workoutPlanDto = new WorkoutPlanDto();
//        List<WorkoutRoutine> workoutRoutineList = new ArrayList<>();
//
//        for (WorkoutRoutineDto workoutRoutineDto : workoutRoutineDtoList) {
//            workoutRepository.saveWorkoutRoutine(workoutRoutineDto); // 루틴 DB에 저장 //
//            workoutRoutineList.add(workoutRoutineDto.toEntity(workoutRepository.findByWorkoutId(workoutRoutineDto.getWorkoutId()).toEntity())); // 값 찍어보기
//        }
//        workoutPlanDto.setWorkoutRoutineList(workoutRoutineList);
//        workoutRepository.saveWorkoutPlan(workoutPlanDto); // 플랜 DB에 저장 //
//
//        return workoutPlanDto;
//    }
    // ================= //
    // 여기서부터 다시 작업하기 //
    // ================= //
    @Override
    public WorkoutPlanDto addWorkoutPlan(List<WorkoutRoutineDto> workoutRoutineDtoList) {
        WorkoutPlanDto workoutPlanDto = new WorkoutPlanDto();
        List<WorkoutRoutine> workoutRoutineList = new ArrayList<>();

        // WorkoutRoutineDto를 엔티티로 변환하여 리스트에 추가
        for (WorkoutRoutineDto workoutRoutineDto : workoutRoutineDtoList) {
            // WorkoutRoutine 엔티티 변환
            Workout workout = workoutRepository.findByWorkoutId(workoutRoutineDto.getWorkoutId()).toEntity(); // workout 찾기
            WorkoutRoutine workoutRoutine = workoutRoutineDto.toEntity(workout);

            workoutRoutineList.add(workoutRoutine); // 루틴 엔티티를 리스트에 추가 (루프 후에 일괄 저장)
        }
//        User user = userRepository.findByUserId(workoutPlanDto.getUser_id()); // UserRepository에 findByUserId 추가해야함.
        User user = new User();
        WorkoutPlan workoutPlan = workoutPlanDto.toEntity(user); // WorkoutPlan 엔티티 변환

        workoutPlanDto.setWorkoutRoutineList(workoutRoutineList);  // for문으로 변환한 엔티티 plan객체에 추가

        workoutPlanRepository.save(workoutPlan); // WorkoutPlan 저장

        workoutRoutineRepository.saveAll(workoutRoutineList); // 변환한 routine리스트 한 번에 저장

        return workoutPlanDto;
    }

}
