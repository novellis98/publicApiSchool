package com.spring.criteria.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.criteria.training.model.Enrollment;
import com.spring.criteria.training.model.key.EnrollmentId;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {

	// controlla se esiste già una iscrizione
	boolean existsById(EnrollmentId id);

	boolean existsByIdStudentIdAndIdCourseId(Long studentId, Long courseId);

	boolean existsByStudentId(Long id);

}