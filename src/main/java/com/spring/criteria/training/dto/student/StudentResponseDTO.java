package com.spring.criteria.training.dto.student;

import java.util.List;

import com.spring.criteria.training.dto.course.CourseResponseDTO;
import com.spring.criteria.training.enums.StudentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentResponseDTO {
	Long id;
	String firstName;
	String lastName;
	String email;
	StudentStatus status;
	List<CourseResponseDTO> courses;
}
