package com.spring.criteria.training.service;

import org.springframework.data.domain.Page;

import com.spring.criteria.training.dto.course.CourseRequestCreateDTO;
import com.spring.criteria.training.dto.course.CourseRequestUpdateDTO;
import com.spring.criteria.training.dto.course.CourseResponseDTO;
import com.spring.criteria.training.dto.course.CourseSearchDTO;
import com.spring.criteria.training.exception.EntityNotFoundException;

public interface CourseService {

	CourseResponseDTO createCourse(CourseRequestCreateDTO request) throws EntityNotFoundException;

	Page<CourseResponseDTO> getCourses(CourseSearchDTO filter);

	CourseResponseDTO updateCourse(CourseRequestUpdateDTO request) throws EntityNotFoundException;

	CourseResponseDTO getCourse(Long id) throws EntityNotFoundException;

	void deleteCourse(Long id) throws EntityNotFoundException;
}