package com.spring.criteria.training.dto;

import com.spring.criteria.training.constants.AppConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchFilterDTO {

	Integer page = AppConstants.DEFAULT_PAGE;
	Integer size = AppConstants.DEFAULT_SIZE;
	private String sortBy;
	private String direction;
	private String courseName;

}
