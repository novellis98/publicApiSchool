package com.spring.criteria.training.dto.course;

import com.spring.criteria.training.dto.teacher.TeacherSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseSummaryDTO {
	private Long id;
	private String title;
	private TeacherSummaryDTO teacher;

}
