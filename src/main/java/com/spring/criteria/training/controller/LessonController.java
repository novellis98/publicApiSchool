package com.spring.criteria.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.criteria.training.dto.lesson.LessonRequestCreateDTO;
import com.spring.criteria.training.dto.lesson.LessonRequestUpdateDTO;
import com.spring.criteria.training.dto.lesson.LessonResponseDTO;
import com.spring.criteria.training.service.LessonService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("api/lessons")
public class LessonController {
	@Autowired
	private LessonService lessonService;

	@PostMapping
	public ResponseEntity<?> createLesson(@RequestBody LessonRequestCreateDTO request) {

		LessonResponseDTO response;
		try {
			response = lessonService.createLesson(request);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> updateLesson(@RequestBody LessonRequestUpdateDTO request) {

		LessonResponseDTO response;
		try {
			response = lessonService.updateLesson(request);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getLesson(@PathVariable Long id) {

		LessonResponseDTO response;
		try {
			response = lessonService.getLesson(id);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteLesson(@PathVariable Long id) {

		try {
			lessonService.deleteLesson(id);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
