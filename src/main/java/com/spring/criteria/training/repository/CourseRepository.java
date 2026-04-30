package com.spring.criteria.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.criteria.training.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
	// repository base per Course

}