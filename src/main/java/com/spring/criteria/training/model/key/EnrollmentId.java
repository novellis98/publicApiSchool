package com.spring.criteria.training.model.key;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Embeddable
// indica che questa classe rappresenta una chiave composta embeddabile in una entity

public class EnrollmentId implements Serializable {
// deve implementare Serializable perché JPA lo richiede per chiavi composte

	@Column(name = "student_id")
	// mappa la colonna student_id della tabella enrollment
	private Long studentId;

	@Column(name = "course_id")
	// mappa la colonna course_id della tabella enrollment
	private Long courseId;

}