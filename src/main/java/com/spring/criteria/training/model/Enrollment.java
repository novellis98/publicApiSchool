package com.spring.criteria.training.model;

import java.time.LocalDateTime;

import com.spring.criteria.training.enums.StatusCourse;
import com.spring.criteria.training.model.key.EnrollmentId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "enrollment")
@Data
public class Enrollment {

	@EmbeddedId
	// indica che la chiave primaria è composta
	private EnrollmentId id;
	// contiene student_id e course_id

	@ManyToOne(fetch = FetchType.LAZY)
	// molti enrollment appartengono a uno student
	@MapsId("studentId")
	// collega la FK con il campo studentId della chiave composta
	@JoinColumn(name = "student_id")
	// colonna FK student_id
	private Student student;

	@ManyToOne(fetch = FetchType.LAZY)
	// molti enrollment appartengono a un course
	@MapsId("courseId")
	// collega la FK con il campo courseId della chiave composta
	@JoinColumn(name = "course_id")
	// colonna FK course_id
	private Course course;

	@Column(name = "enrollment_date", nullable = false)
	private LocalDateTime enrollmentDate;

	@Column
	private Integer progress;

	@Column(nullable = false)
	private StatusCourse status;

}