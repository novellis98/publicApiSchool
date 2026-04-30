package com.spring.criteria.training.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.criteria.training.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	boolean existsByEmail(String email);

	@Query("""
			    SELECT s FROM Student s
			    LEFT JOIN FETCH s.enrollments e
			    LEFT JOIN FETCH e.course
			    WHERE s.id = :id
			""")
	Optional<Student> findByIdWithCourses(@Param("id") Long id);

}
