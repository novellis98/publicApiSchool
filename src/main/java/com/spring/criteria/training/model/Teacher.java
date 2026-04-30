package com.spring.criteria.training.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "teacher")
@Getter
@Setter
public class Teacher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String subject;

	@OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
	// relazione uno a molti con Course
	// mappedBy indica che il proprietario della relazione è il campo teacher nella
	// entity Course
	// fetch LAZY evita che i corsi vengano caricati automaticamente

	private List<Course> courses = new ArrayList<>();
	// lista dei corsi creati dal docente
	// inizializzata per evitare NullPointerException
}