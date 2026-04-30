package com.spring.criteria.training.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.spring.criteria.training.dto.teacher.TeacherRequestCreateDTO;
import com.spring.criteria.training.dto.teacher.TeacherResponseDTO;
import com.spring.criteria.training.dto.teacher.TeacherSummaryDTO;
import com.spring.criteria.training.model.Teacher;

@Component
public class TeacherMapper {

	public TeacherResponseDTO toDTO(Teacher entity) {
		TeacherResponseDTO dto = new TeacherResponseDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setEmail(entity.getEmail());
		dto.setSubject(entity.getSubject());
		if (entity.getCourses().isEmpty()) {
			dto.setCourses(List.of());
		}
		return dto;
	}

	public Teacher toEntity(TeacherRequestCreateDTO dto) {
		Teacher entity = new Teacher();
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setSubject(dto.getSubject());
		return entity;
	}

	public TeacherSummaryDTO toDTOSummary(Teacher entity) {
		TeacherSummaryDTO dto = new TeacherSummaryDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setEmail(entity.getEmail());
		dto.setSubject(entity.getSubject());
		return dto;

	}
}
