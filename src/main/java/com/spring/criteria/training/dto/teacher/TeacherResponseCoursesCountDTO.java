package com.spring.criteria.training.dto.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
// usato per la get per vedere un teacher quanti corsi ha assegnati
public class TeacherResponseCoursesCountDTO {

	Long id;
	String name;
	String email;
	String subject;
	Integer coursesCount;
}
