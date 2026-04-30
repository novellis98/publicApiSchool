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

import com.spring.criteria.training.dto.teacher.TeacherRequestCreateDTO;
import com.spring.criteria.training.dto.teacher.TeacherRequestUpdateDTO;
import com.spring.criteria.training.dto.teacher.TeacherResponseCoursesCountDTO;
import com.spring.criteria.training.dto.teacher.TeacherResponseDTO;
import com.spring.criteria.training.dto.teacher.TeacherSearchDTO;
import com.spring.criteria.training.exception.EntityNotFoundException;
import com.spring.criteria.training.service.TeacherService;

@RestController
@RequestMapping("api/teachers")
public class TeacherController {

	@Autowired
	private TeacherService teacherService;

	@PostMapping
	public ResponseEntity<TeacherResponseDTO> createTeacher(@RequestBody TeacherRequestCreateDTO request) {

		TeacherResponseDTO response = teacherService.createTeacher(request);

		return new ResponseEntity<TeacherResponseDTO>(response, HttpStatus.CREATED);
	}

	@GetMapping("course-count")
	public ResponseEntity<Page<TeacherResponseCoursesCountDTO>> getTeachersCourseCount(
			@ModelAttribute TeacherSearchDTO dto) {
		Page<TeacherResponseCoursesCountDTO> response = teacherService.getTeachersCourseCount(dto);
		return new ResponseEntity<Page<TeacherResponseCoursesCountDTO>>(response, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> updateTeacher(@RequestBody TeacherRequestUpdateDTO request) {

		TeacherResponseDTO response;
		try {
			response = teacherService.updateTeacher(request);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		return new ResponseEntity<TeacherResponseDTO>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {

		try {
			teacherService.deleteTeacher(id);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		return new ResponseEntity<TeacherResponseDTO>(HttpStatus.NO_CONTENT);
	}

}
