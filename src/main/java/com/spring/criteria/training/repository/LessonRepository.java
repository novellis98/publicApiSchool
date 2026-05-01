package com.spring.criteria.training.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.criteria.training.model.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

	@Query("""
			    SELECT l FROM Lesson l
			    JOIN FETCH l.course c
			    JOIN FETCH c.teacher
			    WHERE l.id = :id
			""")
	Optional<Lesson> findByIdFull(Long id);
}
