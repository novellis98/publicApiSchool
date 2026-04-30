package com.spring.criteria.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.criteria.training.dto.course.CourseRequestCreateDTO;
import com.spring.criteria.training.dto.course.CourseResponseDTO;
import com.spring.criteria.training.dto.course.CourseSearchDTO;
import com.spring.criteria.training.exception.EntityNotFoundException;
import com.spring.criteria.training.service.CourseService;

@RestController
@RequestMapping("api/courses")
public class CourseController {
	@Autowired
	private CourseService courseService;

	@PostMapping
	public ResponseEntity<?> createStudent(@RequestBody CourseRequestCreateDTO request) {

		CourseResponseDTO response = null;
		try {
			response = courseService.createCourse(request);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<Page<CourseResponseDTO>> getCourses(@ModelAttribute CourseSearchDTO request) {
		Page<CourseResponseDTO> response = courseService.getCourses(request);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
