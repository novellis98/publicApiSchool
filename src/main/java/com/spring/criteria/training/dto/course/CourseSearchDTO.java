package com.spring.criteria.training.dto.course;

import com.spring.criteria.training.dto.SearchFilterDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseSearchDTO extends SearchFilterDTO {
	String courseName;
	String teacherName;
}
