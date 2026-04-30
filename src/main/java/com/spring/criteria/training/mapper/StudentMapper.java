package com.spring.criteria.training.mapper;

import org.springframework.stereotype.Component;

import com.spring.criteria.training.dto.student.StudentRequestCreateDTO;
import com.spring.criteria.training.dto.student.StudentRequestUpdateDTO;
import com.spring.criteria.training.dto.student.StudentResponseDTO;
import com.spring.criteria.training.dto.student.StudentSummaryDTO;
import com.spring.criteria.training.enums.StudentStatus;
import com.spring.criteria.training.model.Student;

@Component
public class StudentMapper {

	public Student toEntity(StudentRequestCreateDTO dto) {

		Student entity = new Student();
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		entity.setStatus(StudentStatus.ACTIVE);
		return entity;

	}

	public StudentResponseDTO toDTO(Student entity) {

		StudentResponseDTO dto = new StudentResponseDTO();
		dto.setId(entity.getId());
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		dto.setEmail(entity.getEmail());
		dto.setStatus(entity.getStatus());
		return dto;
	}

	public StudentSummaryDTO toSummaryDTO(Student entity) {
		StudentSummaryDTO dto = new StudentSummaryDTO();
		dto.setId(entity.getId());
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		dto.setEmail(dto.getEmail());
		dto.setStatus(entity.getStatus());
		return dto;
	}

	public Student toEntityUpdating(StudentRequestUpdateDTO dto) {
		Student entity = new Student();
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		return entity;
	}
}
