package com.spring.criteria.training.dto.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeacherRequestCreateDTO {
	String name;
	String email;
	String subject;
}
