package com.bit.finalproject.service;

import com.bit.finalproject.dto.WorkoutDto;
import com.bit.finalproject.dto.WorkoutPlanDto;
import com.bit.finalproject.dto.WorkoutRoutineDto;

import java.util.List;

public interface WorkoutService {

    List<WorkoutDto> findByCategory(WorkoutDto workoutDto);

    List<WorkoutRoutineDto> findByWorkoutId(List<WorkoutDto> workoutDtoList);

    WorkoutPlanDto addWorkoutPlan(List<WorkoutRoutineDto> workoutRoutineDtoList);

    WorkoutDto setWorkout(WorkoutDto workoutDto);
}
