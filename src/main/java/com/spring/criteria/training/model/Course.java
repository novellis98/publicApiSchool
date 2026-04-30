package com.spring.criteria.training.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "course")
@Getter
@Setter
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	// relazione molti a uno verso Teacher
	// molti corsi possono appartenere a un docente
	// fetch lazy evita caricamento automatico

	@JoinColumn(name = "teacher_id", nullable = false)
	// specifica la colonna FK nel database
	// teacher_id è la colonna nella tabella course

	private Teacher teacher;
	// rappresenta il docente del corso

	@OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
	// relazione uno a molti verso Lesson
	// mappedBy indica che il proprietario della relazione è Lesson

	private Set<Lesson> lessons = new HashSet<>();
	// lista delle lezioni del corso

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}