package com.spring.criteria.training.dto.course;

import java.util.List;

import com.spring.criteria.training.dto.lesson.LessonSummaryDTO;
import com.spring.criteria.training.dto.teacher.TeacherSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseResponseDTO {
	private Long id;
	private String title;
	private TeacherSummaryDTO teacher;
	private List<LessonSummaryDTO> lessons;

}
