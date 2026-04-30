package com.spring.criteria.training.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnrollmentRequestCreateDTO {
	private Long studentId;

	private Long courseId;
}
