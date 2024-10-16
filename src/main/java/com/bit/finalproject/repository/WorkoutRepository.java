package com.bit.finalproject.repository;

import com.bit.finalproject.dto.WorkoutDto;
import com.bit.finalproject.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<WorkoutDto> findByMainCategory(WorkoutDto workoutDto);

    WorkoutDto findByWorkoutId(Long workoutId);
}
