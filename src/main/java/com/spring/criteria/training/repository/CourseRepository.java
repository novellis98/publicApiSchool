package com.spring.criteria.training.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.criteria.training.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
	// repository base per Course
	@Query("SELECT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c.id = :id")
	Optional<Course> findByIdWithLessons(Long id);
}