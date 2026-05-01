package com.spring.criteria.training.service;

import com.spring.criteria.training.dto.lesson.LessonRequestCreateDTO;
import com.spring.criteria.training.dto.lesson.LessonRequestUpdateDTO;
import com.spring.criteria.training.dto.lesson.LessonResponseDTO;

public interface LessonService {
	LessonResponseDTO createLesson(LessonRequestCreateDTO request);

	LessonResponseDTO updateLesson(LessonRequestUpdateDTO request);

	LessonResponseDTO getLesson(Long id);

	void deleteLesson(Long id);
}
