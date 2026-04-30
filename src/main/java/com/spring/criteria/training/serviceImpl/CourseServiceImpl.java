package com.spring.criteria.training.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.spring.criteria.training.constants.AppConstants;
import com.spring.criteria.training.dto.course.CourseRequestCreateDTO;
import com.spring.criteria.training.dto.course.CourseResponseDTO;
import com.spring.criteria.training.dto.course.CourseSearchDTO;
import com.spring.criteria.training.dto.lesson.LessonSummaryDTO;
import com.spring.criteria.training.dto.teacher.TeacherSummaryDTO;
import com.spring.criteria.training.exception.EntityNotFoundException;
import com.spring.criteria.training.mapper.CourseMapper;
import com.spring.criteria.training.mapper.TeacherMapper;
import com.spring.criteria.training.model.Course;
import com.spring.criteria.training.model.Teacher;
import com.spring.criteria.training.repository.CourseRepository;
import com.spring.criteria.training.repository.TeacherRepository;
import com.spring.criteria.training.service.CourseService;

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
public class CourseServiceImpl implements CourseService {

	@PersistenceContext // inject EntityManager (fondamentale per Criteria API)
	private EntityManager entityManager;

	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private CourseMapper courseMapper;
	@Autowired
	private TeacherMapper teacherMapper;

	@Override
	public CourseResponseDTO createCourse(CourseRequestCreateDTO request) throws EntityNotFoundException {
		Course course = new Course();
		course.setTitle(request.getTitle());
		Teacher teacher = teacherRepository.findById(request.getTeacherId())
				.orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

		course.setTeacher(teacher);
		Course saved = courseRepository.save(course);

		CourseResponseDTO response = courseMapper.toDTO(saved);
		response.setLessons(List.of());

		TeacherSummaryDTO teacherDTO = teacherMapper.toDTOSummary(teacher);
		response.setTeacher(teacherDTO);
		return response;
	}

	@Override
	public Page<CourseResponseDTO> getCourses(CourseSearchDTO filter) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		int page = filter.getPage() != null ? filter.getPage() : AppConstants.DEFAULT_PAGE;
		int size = filter.getSize() != null ? filter.getSize() : AppConstants.DEFAULT_SIZE;

		// ── STEP 1: IDs paginati con Tuple ───────────────────
		CriteriaQuery<Tuple> idsQuery = cb.createTupleQuery();
		Root<Course> idsRoot = idsQuery.from(Course.class);
		Join<Course, Teacher> teacherJoin = idsRoot.join("teacher", JoinType.LEFT);

		String sortBy = (filter.getSortBy() != null && SORTABLE_FIELDS.contains(filter.getSortBy()))
				? filter.getSortBy()
				: "id";

		idsQuery.multiselect(idsRoot.get("id").alias("id"), idsRoot.get(sortBy).alias("sortField")).distinct(true);

		idsQuery.where(buildPredicates(cb, idsRoot, teacherJoin, filter).toArray(new Predicate[0]));
		applySort(cb, idsQuery, idsRoot, filter);

		List<Long> ids = entityManager.createQuery(idsQuery).setFirstResult(page * size).setMaxResults(size)
				.getResultList().stream().map(t -> t.get("id", Long.class)).toList();

		if (ids.isEmpty())
			return Page.empty(PageRequest.of(page, size));

		// ── STEP 2: Count ─────────────────────────────────────
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Course> countRoot = countQuery.from(Course.class);
		Join<Course, Teacher> countTeacherJoin = countRoot.join("teacher", JoinType.LEFT);

		countQuery.select(cb.countDistinct(countRoot));
		countQuery.where(buildPredicates(cb, countRoot, countTeacherJoin, filter).toArray(new Predicate[0]));

		Long total = entityManager.createQuery(countQuery).getSingleResult();

		// ── STEP 3: Fetch completo ────────────────────────────
		CriteriaQuery<Course> fetchQuery = cb.createQuery(Course.class);
		Root<Course> fetchRoot = fetchQuery.from(Course.class);

		fetchRoot.fetch("teacher", JoinType.LEFT);
		fetchRoot.fetch("lessons", JoinType.LEFT);

		fetchQuery.select(fetchRoot).distinct(true);
		fetchQuery.where(fetchRoot.get("id").in(ids));
		applySort(cb, fetchQuery, fetchRoot, filter);

		List<Course> courses = entityManager.createQuery(fetchQuery).getResultList();

		// ── STEP 4: Mapping ───────────────────────────────────
		List<CourseResponseDTO> response = courses.stream().map(c -> {
			CourseResponseDTO dto = courseMapper.toDTO(c);
			dto.setTeacher(teacherMapper.toDTOSummary(c.getTeacher()));
			dto.setLessons(c.getLessons().stream().map(l -> {
				LessonSummaryDTO lesson = new LessonSummaryDTO();
				lesson.setId(l.getId());
				lesson.setTitle(l.getTitle());
				return lesson;
			}).toList());
			return dto;
		}).toList();

		return new PageImpl<>(response, PageRequest.of(page, size), total);
	}

	// ── HELPERS ───────────────────────────────────────────
	private static final Set<String> SORTABLE_FIELDS = Set.of("id", "title");

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Course> root, Join<Course, Teacher> teacherJoin,
			CourseSearchDTO filter) {
		List<Predicate> predicates = new ArrayList<>();

		if (filter.getCourseName() != null && !filter.getCourseName().isBlank()) {
			predicates.add(cb.like(cb.lower(root.get("title")), "%" + filter.getCourseName().toLowerCase() + "%"));
		}

		if (filter.getTeacherName() != null && !filter.getTeacherName().isBlank()) {
			predicates.add(
					cb.like(cb.lower(teacherJoin.get("firstName")), "%" + filter.getTeacherName().toLowerCase() + "%"));
		}

		return predicates;
	}

	private void applySort(CriteriaBuilder cb, CriteriaQuery<?> query, Root<Course> root, CourseSearchDTO filter) {
		if (filter.getSortBy() != null) {
			if ("DESC".equalsIgnoreCase(filter.getDirection())) {
				query.orderBy(cb.desc(root.get(filter.getSortBy())));
			} else {
				query.orderBy(cb.asc(root.get(filter.getSortBy())));
			}
		}
	}

}
