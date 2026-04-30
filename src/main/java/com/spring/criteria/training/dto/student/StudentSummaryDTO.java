package com.spring.criteria.training.dto.student;

import com.spring.criteria.training.enums.StudentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentSummaryDTO {
	Long id;
	String firstName;
	String lastName;
	String email;
	StudentStatus status;
}
