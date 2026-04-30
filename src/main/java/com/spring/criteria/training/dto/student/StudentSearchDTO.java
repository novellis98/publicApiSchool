package com.spring.criteria.training.dto.student;

import com.spring.criteria.training.dto.SearchFilterDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentSearchDTO extends SearchFilterDTO {
	String firstName;
	String lastName;
	String status;

}
