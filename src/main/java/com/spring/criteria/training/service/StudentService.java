package com.spring.criteria.training.service;

import org.springframework.data.domain.Page;

import com.spring.criteria.training.dto.student.StudentRequestCreateDTO;
import com.spring.criteria.training.dto.student.StudentRequestUpdateDTO;
import com.spring.criteria.training.dto.student.StudentResponseDTO;
import com.spring.criteria.training.dto.student.StudentSearchDTO;
import com.spring.criteria.training.exception.EntityNotFoundException;
import com.spring.criteria.training.exception.SqlException;

public interface StudentService {

	StudentResponseDTO createStudent(StudentRequestCreateDTO request) throws SqlException;

	StudentResponseDTO updateStudent(StudentRequestUpdateDTO request) throws EntityNotFoundException;

	StudentResponseDTO getStudent(Long id) throws EntityNotFoundException;

	void deleteStudent(Long id) throws EntityNotFoundException, SqlException;

	Page<StudentResponseDTO> getStudents(StudentSearchDTO filter);
}
