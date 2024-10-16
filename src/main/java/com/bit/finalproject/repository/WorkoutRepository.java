package com.bit.finalproject.repository;

import com.bit.finalproject.dto.WorkoutDto;
import com.bit.finalproject.dto.WorkoutPlanDto;
import com.bit.finalproject.dto.WorkoutRoutineDto;
import com.bit.finalproject.entity.Workout;
import com.bit.finalproject.entity.WorkoutRoutine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<WorkoutDto> findByCategory(String mainCategory, String subCategory, int favoriteWorkout);

    WorkoutDto findByWorkoutId(Long workoutId);

    void saveWorkoutPlan(WorkoutPlanDto workoutPlanDto);

    void saveWorkoutRoutine(WorkoutRoutineDto workoutRoutineDto);

    List<WorkoutDto> findByMainCategory(WorkoutDto workoutDto);

}
