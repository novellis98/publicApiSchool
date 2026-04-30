package com.spring.criteria.training.dto.teacher;

import com.spring.criteria.training.dto.SearchFilterDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeacherSearchDTO extends SearchFilterDTO {
	String name;
	String subject;
	Integer minCourses;
	Integer maxCourses;
}
