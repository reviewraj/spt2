package com.tekpyramid.sp.entity;

import lombok.Data;

@Data
public class CustomField {
	private String label;
	private String fieldType;
	private boolean mandatory;
	private String placeholder;
	private String defaultValue;
	private boolean visibleToCustomer;
	private boolean visibleToInternal;
	private Integer minLength;
	private Integer maxLength;
}