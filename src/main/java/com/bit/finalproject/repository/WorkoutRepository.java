package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    Workout findByWorkoutId(Long workoutId);

    List<Workout> findAllByMainCategory(String mainCategory);
}
