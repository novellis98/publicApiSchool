package com.spring.criteria.training.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LessonRequestCreateDTO {
	String title;
	Long courseId;
}
