package com.spring.criteria.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.criteria.training.dto.student.StudentRequestCreateDTO;
import com.spring.criteria.training.dto.student.StudentRequestUpdateDTO;
import com.spring.criteria.training.dto.student.StudentResponseDTO;
import com.spring.criteria.training.dto.student.StudentSearchDTO;
import com.spring.criteria.training.exception.EntityNotFoundException;
import com.spring.criteria.training.exception.SqlException;
import com.spring.criteria.training.service.StudentService;

@RestController
@RequestMapping("api/students")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@PostMapping
	public ResponseEntity<?> createStudent(@RequestBody StudentRequestCreateDTO request) {

		StudentResponseDTO response;
		try {
			response = studentService.createStudent(request);
		} catch (SqlException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> updateStudent(@RequestBody StudentRequestUpdateDTO request) {

		StudentResponseDTO response;
		try {
			response = studentService.updateStudent(request);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> updateStudent(@PathVariable Long id) {

		try {
			studentService.deleteStudent(id);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (SqlException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping
	public ResponseEntity<Page<StudentResponseDTO>> getStudents(@ModelAttribute StudentSearchDTO filter) {
		Page<StudentResponseDTO> response = studentService.getStudents(filter);
		return new ResponseEntity<Page<StudentResponseDTO>>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getStudent(@PathVariable Long id) {
		StudentResponseDTO response;
		try {
			response = studentService.getStudent(id);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
