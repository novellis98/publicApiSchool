package com.spring.criteria.training.mapper;

import org.springframework.stereotype.Component;

import com.spring.criteria.training.dto.course.CourseRequestCreateDTO;
import com.spring.criteria.training.dto.course.CourseResponseDTO;
import com.spring.criteria.training.dto.course.CourseSummaryDTO;
import com.spring.criteria.training.model.Course;

@Component
public class CourseMapper {

	public Course toEntity(CourseRequestCreateDTO dto) {
		Course entity = new Course();
		entity.setTitle(dto.getTitle());
		return entity;
	}

	public CourseResponseDTO toDTO(Course entity) {
		CourseResponseDTO dto = new CourseResponseDTO();
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		return dto;
	}

	public CourseSummaryDTO toSummaryDTO(Course entity) {
		CourseSummaryDTO dto = new CourseSummaryDTO();
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		return dto;
	}
}
