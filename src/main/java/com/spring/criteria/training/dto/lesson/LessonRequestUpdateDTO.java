package com.spring.criteria.training.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LessonRequestUpdateDTO {
	Long id;
	String title;
	Long courseId;
}
