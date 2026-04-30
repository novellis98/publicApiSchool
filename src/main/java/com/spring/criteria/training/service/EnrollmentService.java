package com.spring.criteria.training.service;

import com.spring.criteria.training.dto.enrollment.EnrollmentRequestCreateDTO;
import com.spring.criteria.training.dto.enrollment.EnrollmentResponseDTO;
import com.spring.criteria.training.exception.ConflitException;
import com.spring.criteria.training.exception.EntityNotFoundException;

public interface EnrollmentService {
	EnrollmentResponseDTO enrollStudent(EnrollmentRequestCreateDTO request)
			throws EntityNotFoundException, ConflitException;
}