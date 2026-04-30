package com.spring.criteria.training.dto.enrollment;

import com.spring.criteria.training.dto.course.CourseSummaryDTO;
import com.spring.criteria.training.dto.student.StudentSummaryDTO;
import com.spring.criteria.training.enums.StatusCourse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnrollmentResponseDTO {

	private StudentSummaryDTO student;
	private CourseSummaryDTO course;

	private StatusCourse status;
	private Integer progress;
}