package com.spring.criteria.training.service;

import org.springframework.data.domain.Page;

import com.spring.criteria.training.dto.teacher.TeacherRequestCreateDTO;
import com.spring.criteria.training.dto.teacher.TeacherRequestUpdateDTO;
import com.spring.criteria.training.dto.teacher.TeacherResponseCoursesCountDTO;
import com.spring.criteria.training.dto.teacher.TeacherResponseDTO;
import com.spring.criteria.training.dto.teacher.TeacherSearchDTO;
import com.spring.criteria.training.exception.EntityNotFoundException;

public interface TeacherService {

	TeacherResponseDTO createTeacher(TeacherRequestCreateDTO request);

	Page<TeacherResponseCoursesCountDTO> getTeachersCourseCount(TeacherSearchDTO dto);

	TeacherResponseDTO updateTeacher(TeacherRequestUpdateDTO request) throws EntityNotFoundException;

	void deleteTeacher(Long id) throws EntityNotFoundException;
}
