package com.spring.criteria.training.dto.teacher;

import java.util.List;

import com.spring.criteria.training.dto.course.CourseResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeacherResponseDTO {
	Long id;
	String name;
	String email;
	String subject;
	List<CourseResponseDTO> courses;
}
