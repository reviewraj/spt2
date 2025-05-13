package com.tekpyramid.sp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tekpyramid.sp.utility.AuditableDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@Document
@EqualsAndHashCode(callSuper = false)
public class Privilege extends AuditableDocument{
	@Id
	private String privilegeId;
	private String privilege;
}
