package com.spring.criteria.training.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.criteria.training.constants.AppConstants;
import com.spring.criteria.training.dto.course.CourseResponseDTO;
import com.spring.criteria.training.dto.lesson.LessonSummaryDTO;
import com.spring.criteria.training.dto.student.StudentRequestCreateDTO;
import com.spring.criteria.training.dto.student.StudentRequestUpdateDTO;
import com.spring.criteria.training.dto.student.StudentResponseDTO;
import com.spring.criteria.training.dto.student.StudentSearchDTO;
import com.spring.criteria.training.enums.StudentStatus;
import com.spring.criteria.training.exception.EntityNotFoundException;
import com.spring.criteria.training.exception.SqlException;
import com.spring.criteria.training.mapper.CourseMapper;
import com.spring.criteria.training.mapper.StudentMapper;
import com.spring.criteria.training.model.Course;
import com.spring.criteria.training.model.Enrollment;
import com.spring.criteria.training.model.Student;
import com.spring.criteria.training.repository.CourseRepository;
import com.spring.criteria.training.repository.EnrollmentRepository;
import com.spring.criteria.training.repository.StudentRepository;
import com.spring.criteria.training.service.StudentService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private EnrollmentRepository enrollmentRepository;
	@Autowired
	private StudentMapper studentMapper;
	@Autowired
	private CourseMapper courseMapper;
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public StudentResponseDTO createStudent(StudentRequestCreateDTO request) throws SqlException {

		boolean exists = studentRepository.existsByEmail(request.getEmail().toLowerCase());

		if (exists) {
			throw new SqlException("User already exist with this email");
		}

		Student saved = studentMapper.toEntity(request);
		Student studentSaved = studentRepository.save(saved);

		StudentResponseDTO response = studentMapper.toDTO(studentSaved);
		// trasformo enrollments in courses
		response.setCourses(saved.getEnrollments() != null ? saved.getEnrollments().stream().map(enrollment -> {

			// creo DTO corso
			CourseResponseDTO course = new CourseResponseDTO();

			// prendo id corso
			course.setId(enrollment.getCourse().getId());

			// prendo titolo corso
			course.setTitle(enrollment.getCourse().getTitle());

			return course;

		}).toList() : List.of());

		return response;
	}

	@Override
	@Transactional
	public StudentResponseDTO updateStudent(StudentRequestUpdateDTO request) throws EntityNotFoundException {

		Student studentInDb = studentRepository.findByIdWithCourses(request.getId())
				.orElseThrow(() -> new EntityNotFoundException("Student not found"));
		Student student = studentMapper.toEntityUpdating(request);
//		 setto lo stato con il valore preso dal db. //LO STUDENTE non deve cambiarselo da solo
		student.setStatus(studentInDb.getStatus());

		Student saved = studentRepository.save(student);

		StudentResponseDTO response = studentMapper.toDTO(saved);
		List<CourseResponseDTO> courses = studentInDb.getEnrollments().stream()
				.map(e -> courseMapper.toDTO(e.getCourse())).toList();

		response.setCourses(courses);

		return response;
	}

	@Override
	public void deleteStudent(Long id) throws EntityNotFoundException, SqlException {

		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Student not found"));

		boolean enrollment = enrollmentRepository.existsByStudentId(id);
		if (enrollment) {
			throw new SqlException("User associated with a course");
		}

		studentRepository.delete(student);
	}

	@Override
	public Page<StudentResponseDTO> getStudents(StudentSearchDTO filter) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		int page = filter.getPage() != null ? filter.getPage() : AppConstants.DEFAULT_PAGE;
		int size = filter.getSize() != null ? filter.getSize() : AppConstants.DEFAULT_SIZE;

		// QUERY ID PAGINATI con Tuple
		CriteriaQuery<Tuple> idsQuery = cb.createTupleQuery();
		Root<Student> idsRoot = idsQuery.from(Student.class);
		Join<Student, Enrollment> idsEnrollJoin = idsRoot.join("enrollments", JoinType.LEFT);
		Join<Enrollment, Course> idsCoursJoin = idsEnrollJoin.join("course", JoinType.LEFT);

		String sortBy = (filter.getSortBy() != null && SORTABLE_FIELDS.contains(filter.getSortBy()))
				? filter.getSortBy()
				: "id";

		idsQuery.multiselect(idsRoot.get("id").alias("id"), idsRoot.get(sortBy).alias("sortField")).distinct(true);

		idsQuery.where(buildPredicates(cb, idsRoot, idsEnrollJoin, idsCoursJoin, filter).toArray(new Predicate[0]));

		applySorting(cb, idsQuery, idsRoot, filter);

		List<Long> studentIds = entityManager.createQuery(idsQuery).setFirstResult(page * size).setMaxResults(size)
				.getResultList().stream().map(t -> t.get("id", Long.class)).toList();
		if (studentIds.isEmpty()) {
			return new PageImpl<>(List.of(), PageRequest.of(page, size), 0);
		}

		// QUERY COUNT
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Student> countRoot = countQuery.from(Student.class);
		Join<Student, Enrollment> countEnrollJoin = countRoot.join("enrollments", JoinType.LEFT);
		Join<Enrollment, Course> countCourseJoin = countEnrollJoin.join("course", JoinType.LEFT);

		List<Predicate> countPredicates = buildPredicates(cb, countRoot, countEnrollJoin, countCourseJoin, filter);

		countQuery.select(cb.countDistinct(countRoot));
		countQuery.where(countPredicates.toArray(new Predicate[0]));

		Long total = entityManager.createQuery(countQuery).getSingleResult();

		// QUERY FETCH COMPLETA
		CriteriaQuery<Student> fetchQuery = cb.createQuery(Student.class);
		Root<Student> fetchRoot = fetchQuery.from(Student.class);

		fetchRoot.fetch("enrollments", JoinType.LEFT).fetch("course", JoinType.LEFT).fetch("lessons", JoinType.LEFT);

		fetchQuery.select(fetchRoot).distinct(true);
		fetchQuery.where(fetchRoot.get("id").in(studentIds));

		applySorting(cb, fetchQuery, fetchRoot, filter);

		List<Student> students = entityManager.createQuery(fetchQuery).getResultList();

		List<StudentResponseDTO> response = students.stream().map(s -> {
			StudentResponseDTO dto = studentMapper.toDTO(s);

			List<CourseResponseDTO> coursesDTO = s.getEnrollments().stream().map(e -> {
				CourseResponseDTO courseDTO = courseMapper.toDTO(e.getCourse());
				List<LessonSummaryDTO> lessons = e.getCourse().getLessons().stream().map(l -> {
					LessonSummaryDTO d = new LessonSummaryDTO();
					d.setId(l.getId());
					d.setTitle(l.getTitle());
					return d;
				}).toList();
				courseDTO.setLessons(lessons);
				return courseDTO;
			}).toList();

			dto.setCourses(coursesDTO);
			return dto;
		}).toList();

		return new PageImpl<>(response, PageRequest.of(page, size), total);
	}

// UTILS
	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Student> root,
			Join<Student, Enrollment> enrollJoin, Join<Enrollment, Course> courseJoin, StudentSearchDTO filter) {
		List<Predicate> predicates = new ArrayList<>();

		if (filter.getCourseName() != null) {
			predicates
					.add(cb.like(cb.lower(courseJoin.get("title")), "%" + filter.getCourseName().toLowerCase() + "%"));
		}
		// nel predicato
		if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
			try {
				StudentStatus status = StudentStatus.valueOf(filter.getStatus().toUpperCase());
				predicates.add(cb.equal(root.get("status"), status));
			} catch (IllegalArgumentException e) {
				// valore non valido → ignora
			}
		}
		if (filter.getFirstName() != null) {
			predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + filter.getFirstName().toLowerCase() + "%"));
		}
		if (filter.getLastName() != null) {
			predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + filter.getLastName().toLowerCase() + "%"));
		}
		return predicates;
	}

	private static final Set<String> SORTABLE_FIELDS = Set.of("id", "firstName", "lastName", "email", "status");

	private void applySorting(CriteriaBuilder cb, CriteriaQuery<?> query, Root<Student> root, StudentSearchDTO filter) {
		if (filter.getSortBy() != null) {
			if ("DESC".equalsIgnoreCase(filter.getDirection())) {
				query.orderBy(cb.desc(root.get(filter.getSortBy())));
			} else {
				query.orderBy(cb.asc(root.get(filter.getSortBy())));
			}
		}
	}

	@Override
	public StudentResponseDTO getStudent(Long id) throws EntityNotFoundException {
		Student student = studentRepository.findByIdWithCourses(id)
				.orElseThrow(() -> new EntityNotFoundException("Student not found"));
		StudentResponseDTO response = studentMapper.toDTO(student);

		List<CourseResponseDTO> courses = student.getEnrollments().stream().map(e -> courseMapper.toDTO(e.getCourse()))
				.toList();
		response.setCourses(courses);
		return response;
	}
}
