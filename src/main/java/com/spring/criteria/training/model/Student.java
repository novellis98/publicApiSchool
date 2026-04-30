package com.spring.criteria.training.model;

import java.util.HashSet;
import java.util.Set;

import com.spring.criteria.training.enums.StudentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	// colonna name obbligatoria
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private StudentStatus status;

	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
	// uno studente può avere molte iscrizioni
	// mappedBy indica che la relazione è gestita da Enrollment.student
	private Set<Enrollment> enrollments = new HashSet<>();
	// lista delle iscrizioni dello studente

}