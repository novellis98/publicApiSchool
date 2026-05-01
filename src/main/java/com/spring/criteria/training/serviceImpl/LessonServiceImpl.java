package com.spring.criteria.training.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.criteria.training.dto.course.CourseSummaryDTO;
import com.spring.criteria.training.dto.lesson.LessonRequestCreateDTO;
import com.spring.criteria.training.dto.lesson.LessonRequestUpdateDTO;
import com.spring.criteria.training.dto.lesson.LessonResponseDTO;
import com.spring.criteria.training.mapper.CourseMapper;
import com.spring.criteria.training.mapper.LessonMapper;
import com.spring.criteria.training.mapper.TeacherMapper;
import com.spring.criteria.training.model.Course;
import com.spring.criteria.training.model.Lesson;
import com.spring.criteria.training.model.Teacher;
import com.spring.criteria.training.repository.CourseRepository;
import com.spring.criteria.training.repository.LessonRepository;
import com.spring.criteria.training.service.LessonService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LessonServiceImpl implements LessonService {

	@Autowired
	private LessonRepository lessonRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private LessonMapper lessonMapper;
	@Autowired
	private CourseMapper courseMapper;
	@Autowired
	private TeacherMapper teacherMapper;

	@Override
	public LessonResponseDTO createLesson(LessonRequestCreateDTO request) {
		Lesson lesson = lessonMapper.toEntity(request);
		Course course = courseRepository.findById(request.getCourseId())
				.orElseThrow(() -> new EntityNotFoundException("Course not found"));
		lesson.setCourse(course);
		Lesson saved = lessonRepository.save(lesson);
// creo la response
		LessonResponseDTO response = lessonMapper.toDTO(saved);
		CourseSummaryDTO courseDTO = courseMapper.toSummaryDTO(course);
		Teacher teacher = course.getTeacher();
		courseDTO.setTeacher(teacherMapper.toDTOSummary(teacher));
		response.setCourse(courseDTO);
		return response;
	}

	@Override
	public LessonResponseDTO getLesson(Long id) {
		Lesson lesson = lessonRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
		Course course = lesson.getCourse();

		LessonResponseDTO response = lessonMapper.toDTO(lesson);
		CourseSummaryDTO courseDTO = courseMapper.toSummaryDTO(course);
		Teacher teacher = course.getTeacher();
		courseDTO.setTeacher(teacherMapper.toDTOSummary(teacher));
		response.setCourse(courseDTO);
		return response;

	}

	@Override
	public void deleteLesson(Long id) {
		Lesson lesson = lessonRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
		lessonRepository.delete(lesson);

	}

	@Transactional
	@Override
	public LessonResponseDTO updateLesson(LessonRequestUpdateDTO request) {

		// recupero lesson
		Lesson lesson = lessonRepository.findByIdFull(request.getId())
				.orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

		// aggiorno campi
		lesson.setTitle(request.getTitle());

		Lesson saved = lessonRepository.save(lesson);

		// recupero course
		Course course = saved.getCourse();

		// costruisco response
		LessonResponseDTO response = lessonMapper.toDTO(saved);
		CourseSummaryDTO courseDTO = courseMapper.toSummaryDTO(course);
// recupero teacher
		Teacher teacher = course.getTeacher();
		courseDTO.setTeacher(teacherMapper.toDTOSummary(teacher));
		response.setCourse(courseDTO);

		return response;
	}
}
