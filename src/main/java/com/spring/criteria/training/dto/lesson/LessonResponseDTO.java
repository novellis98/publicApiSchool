package com.spring.criteria.training.dto.lesson;

import com.spring.criteria.training.dto.course.CourseResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LessonResponseDTO {
	Long id;
	String title;
	CourseResponseDTO course;

}
