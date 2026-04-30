package com.spring.criteria.training.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentRequestCreateDTO {

	String firstName;
	String lastName;
	String email;

}
