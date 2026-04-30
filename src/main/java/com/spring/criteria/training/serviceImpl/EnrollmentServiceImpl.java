package com.spring.criteria.training.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.criteria.training.dto.course.CourseSummaryDTO;
import com.spring.criteria.training.dto.enrollment.EnrollmentRequestCreateDTO;
import com.spring.criteria.training.dto.enrollment.EnrollmentResponseDTO;
import com.spring.criteria.training.dto.student.StudentSummaryDTO;
import com.spring.criteria.training.dto.teacher.TeacherSummaryDTO;
import com.spring.criteria.training.enums.StatusCourse;
import com.spring.criteria.training.exception.ConflitException;
import com.spring.criteria.training.exception.EntityNotFoundException;
import com.spring.criteria.training.mapper.StudentMapper;
import com.spring.criteria.training.model.Course;
import com.spring.criteria.training.model.Enrollment;
import com.spring.criteria.training.model.Student;
import com.spring.criteria.training.model.Teacher;
import com.spring.criteria.training.model.key.EnrollmentId;
import com.spring.criteria.training.repository.CourseRepository;
import com.spring.criteria.training.repository.EnrollmentRepository;
import com.spring.criteria.training.repository.StudentRepository;
import com.spring.criteria.training.repository.TeacherRepository;
import com.spring.criteria.training.service.EnrollmentService;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private StudentMapper studentMapper;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private EnrollmentRepository enrollmentRepository;
	@Autowired
	private TeacherRepository teacherRepository;

	@Transactional // tutto o niente (fondamentale per consistenza dati)
	public EnrollmentResponseDTO enrollStudent(EnrollmentRequestCreateDTO request)
			throws EntityNotFoundException, ConflitException {

//		recupero student
		Student student = studentRepository.findById(request.getStudentId())
				.orElseThrow(() -> new EntityNotFoundException("Student not found"));

		Course course = courseRepository.findById(request.getCourseId())
				.orElseThrow(() -> new EntityNotFoundException("Course not found"));

		if (enrollmentRepository.existsByIdStudentIdAndIdCourseId(request.getStudentId(), request.getCourseId())) {

			throw new ConflitException("Student already assigned to this course");
		}

		Enrollment enrollment = new Enrollment();
		EnrollmentId enrollmentId = new EnrollmentId();
		enrollmentId.setStudentId(student.getId());
		enrollmentId.setCourseId(course.getId());
		enrollment.setStudent(student);
		enrollment.setCourse(course);
		enrollment.setId(enrollmentId);
		enrollment.setEnrollmentDate(LocalDateTime.now());
		enrollment.setProgress(0);
		enrollment.setStatus(StatusCourse.STARTED);
		Enrollment saved = enrollmentRepository.save(enrollment);

		// response
		EnrollmentResponseDTO response = new EnrollmentResponseDTO();
//		student dto
		StudentSummaryDTO studentDTO = studentMapper.toSummaryDTO(student);

//		course dto
		CourseSummaryDTO courseDTO = new CourseSummaryDTO();
		courseDTO.setId(course.getId());
		courseDTO.setTitle(course.getTitle());
//		trovare teachers  per il corso

		Optional<Teacher> teacher = teacherRepository.findById(course.getTeacher().getId());
		if (teacher.isPresent()) {
			TeacherSummaryDTO teacherDTO = new TeacherSummaryDTO();
			teacherDTO.setId(teacher.get().getId());
			teacherDTO.setName(teacher.get().getName());
			courseDTO.setTeacher(teacherDTO);
		} else {
			courseDTO.setTeacher(null);
		}
//		assegno tutto a response
		response.setStudent(studentDTO);
		response.setCourse(courseDTO);
		response.setProgress(saved.getProgress());
		response.setStatus(saved.getStatus());

		return response;

	}

}
