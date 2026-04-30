package com.spring.criteria.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.criteria.training.dto.enrollment.EnrollmentRequestCreateDTO;
import com.spring.criteria.training.dto.enrollment.EnrollmentResponseDTO;
import com.spring.criteria.training.service.EnrollmentService;

@RestController
@RequestMapping("api/enrollment")
public class EnrollmentController {

	@Autowired
	private EnrollmentService enrollmentService;

	@PostMapping
	public ResponseEntity<?> enrollStudent(@RequestBody EnrollmentRequestCreateDTO request) {
		EnrollmentResponseDTO response = null;
		try {
			response = enrollmentService.enrollStudent(request);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}
}
