package com.spring.criteria.training.mapper;

import org.springframework.stereotype.Component;

import com.spring.criteria.training.dto.lesson.LessonRequestCreateDTO;
import com.spring.criteria.training.dto.lesson.LessonResponseDTO;
import com.spring.criteria.training.dto.lesson.LessonSummaryDTO;
import com.spring.criteria.training.model.Lesson;

@Component
public class LessonMapper {
	public Lesson toEntity(LessonRequestCreateDTO dto) {
		Lesson entity = new Lesson();
		entity.setTitle(dto.getTitle());
		return entity;
	}

	public LessonResponseDTO toDTO(Lesson entity) {
		LessonResponseDTO dto = new LessonResponseDTO();
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		return dto;
	}

	public LessonSummaryDTO toSummaryDTO(Lesson entity) {
		LessonSummaryDTO dto = new LessonSummaryDTO();
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		return dto;
	}
}
