package com.spring.criteria.training.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseRequestCreateDTO {

	String title;
	Long teacherId;
}
