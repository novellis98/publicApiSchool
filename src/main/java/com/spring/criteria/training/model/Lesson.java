package com.spring.criteria.training.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "lesson")
@Data
public class Lesson {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false) // colonna obbligatoria
	private String title; // titolo della lezione

	@ManyToOne(fetch = FetchType.LAZY)
	// relazione molti a uno verso Course
	// molte lezioni appartengono a un corso

	@JoinColumn(name = "course_id", nullable = false)
	// definisce la colonna FK course_id

	private Course course;
	// rappresenta il corso a cui appartiene la lezione
}