package com.spring.criteria.training.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.spring.criteria.training.constants.AppConstants;
import com.spring.criteria.training.dto.teacher.TeacherRequestCreateDTO;
import com.spring.criteria.training.dto.teacher.TeacherRequestUpdateDTO;
import com.spring.criteria.training.dto.teacher.TeacherResponseCoursesCountDTO;
import com.spring.criteria.training.dto.teacher.TeacherResponseDTO;
import com.spring.criteria.training.dto.teacher.TeacherSearchDTO;
import com.spring.criteria.training.exception.EntityNotFoundException;
import com.spring.criteria.training.mapper.TeacherMapper;
import com.spring.criteria.training.model.Course;
import com.spring.criteria.training.model.Teacher;
import com.spring.criteria.training.repository.TeacherRepository;
import com.spring.criteria.training.service.TeacherService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class TeacherServiceImpl implements TeacherService {
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private TeacherMapper teacherMapper;
	@PersistenceContext
	private EntityManager entityManager;

	@Override
//	CREATE TEACHER
	public TeacherResponseDTO createTeacher(TeacherRequestCreateDTO request) {

//		mappo la request a entity
		Teacher teacher = teacherMapper.toEntity(request);

		Teacher saved = teacherRepository.save(teacher);
//mappo la entity a dto
		TeacherResponseDTO response = teacherMapper.toDTO(saved);
		return response;
	}

	@Override
//	GET ALL TEACHER WITH COURSES COUNT
	public Page<TeacherResponseCoursesCountDTO> getTeachersCourseCount(TeacherSearchDTO dto) {

		// EntityManager serve per Criteria API
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		// Query finale che ritorna DTO
		CriteriaQuery<Tuple> query = cb.createTupleQuery();

		// ROOT = tabella principale (Teacher)
		Root<Teacher> teacher = query.from(Teacher.class);

		// JOIN con courses (LEFT per includere anche teacher senza corsi)
		Join<Teacher, Course> courses = teacher.join("courses", JoinType.LEFT);

		query.multiselect(teacher.get("id").alias("id"), teacher.get("name").alias("name"),
				teacher.get("email").alias("email"), teacher.get("subject").alias("subject"),
				cb.count(courses.get("id")).alias("coursesCount"));

		List<Predicate> predicates = new ArrayList<>();
		List<Predicate> havingPredicates = new ArrayList<>();

		// WHERE normali
		if (dto.getName() != null && !dto.getName().isEmpty()) {

			predicates.add(cb.like(cb.lower(teacher.get("name")), "%" + dto.getName().toLowerCase() + "%"));
		}
		if (dto.getSubject() != null && !dto.getSubject().isEmpty()) {
			predicates.add(cb.like(cb.lower(teacher.get("subject")), "%" + dto.getSubject().toLowerCase() + "%"));
		}
		query.where(predicates.toArray(new Predicate[0]));
		query.groupBy(teacher.get("id"), teacher.get("name"), teacher.get("email"), teacher.get("subject"));

		// HAVING per le aggregate
		Expression<Long> coursesCount = cb.count(courses.get("id"));

		if (dto.getMinCourses() != null) {
			havingPredicates.add(cb.ge(coursesCount, dto.getMinCourses().longValue()));
		}
		if (dto.getMaxCourses() != null) {
			havingPredicates.add(cb.le(coursesCount, dto.getMaxCourses().longValue()));
		}

		if (!havingPredicates.isEmpty()) {
			query.having(havingPredicates.toArray(new Predicate[0]));
		}

		if ("name".equals(dto.getSortBy())) {

			query.orderBy("DESC".equalsIgnoreCase(dto.getDirection().toString()) ? cb.desc(teacher.get("name"))
					: cb.asc(teacher.get("name")));

		} else if ("subject".equals(dto.getSortBy())) {

			query.orderBy("DESC".equalsIgnoreCase(dto.getDirection()) ? cb.desc(teacher.get("subject"))
					: cb.asc(teacher.get("subject")));
		}

		else if ("coursesCount".equals(dto.getSortBy())) {

			query.orderBy("DESC".equalsIgnoreCase(dto.getDirection()) ? cb.desc(coursesCount) : cb.asc(coursesCount));
		}

		TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);

		int page = dto.getPage() != null ? dto.getPage() : AppConstants.DEFAULT_PAGE;
		int size = dto.getSize() != null ? dto.getSize() : AppConstants.DEFAULT_SIZE;

		typedQuery.setFirstResult(page * size);
		typedQuery.setMaxResults(size);

		List<Tuple> results = typedQuery.getResultList();

		List<TeacherResponseCoursesCountDTO> content = results.stream().map(tuple -> {

			TeacherResponseCoursesCountDTO response = new TeacherResponseCoursesCountDTO();

			response.setId(tuple.get("id", Long.class));
			response.setName(tuple.get("name", String.class));
			response.setEmail(tuple.get("email", String.class));
			response.setSubject(tuple.get("subject", String.class));
			response.setCoursesCount(tuple.get("coursesCount", Long.class).intValue());

			return response;
		}).toList();
//COUNT QUERY
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Teacher> countRoot = countQuery.from(Teacher.class);
		Join<Teacher, Course> countCourses = countRoot.join("courses", JoinType.LEFT);
		List<Predicate> countPredicates = new ArrayList<>();
		List<Predicate> countHavingPredicates = new ArrayList<>();
		if (dto.getName() != null && !dto.getName().isEmpty()) {
			countPredicates.add(cb.like(cb.lower(countRoot.get("name")), "%" + dto.getName().toLowerCase() + "%"));
		}
		if (dto.getSubject() != null && !dto.getSubject().isEmpty()) {
			countPredicates
					.add(cb.like(cb.lower(countRoot.get("subject")), "%" + dto.getSubject().toLowerCase() + "%"));
		}

		countQuery.select(cb.countDistinct(countRoot));
		countQuery.where(countPredicates.toArray(new Predicate[0]));

		// HAVING per la count query
		Expression<Long> countCoursesCount = cb.count(countCourses.get("id"));

		if (dto.getMinCourses() != null) {
			countHavingPredicates.add(cb.ge(countCoursesCount, dto.getMinCourses().longValue()));
		}
		if (dto.getMaxCourses() != null) {
			countHavingPredicates.add(cb.le(countCoursesCount, dto.getMaxCourses().longValue()));
		}

		if (!countHavingPredicates.isEmpty()) {
			countQuery.groupBy(countRoot.get("id"));
			countQuery.having(countHavingPredicates.toArray(new Predicate[0]));
		}

		Long total = entityManager.createQuery(countQuery).getSingleResult();

		return new PageImpl<>(content, PageRequest.of(page, size), total);

	}

	@Override
	public TeacherResponseDTO updateTeacher(TeacherRequestUpdateDTO request) throws EntityNotFoundException {

		Teacher teacher = teacherRepository.findById(request.getId())
				.orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

		teacher.setName(request.getName());
		teacher.setEmail(request.getEmail());
		teacher.setSubject(request.getSubject());
		Teacher saved = teacherRepository.save(teacher);

		TeacherResponseDTO response = teacherMapper.toDTO(saved);

		return response;
	}

	@Override
	public void deleteTeacher(Long id) throws EntityNotFoundException {
		Teacher teacher = teacherRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
		teacherRepository.delete(teacher);
	}

}
