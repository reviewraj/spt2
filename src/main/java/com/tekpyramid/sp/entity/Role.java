package com.tekpyramid.sp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tekpyramid.sp.utility.AuditableDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@Document
@EqualsAndHashCode(callSuper = false)
public class Role extends AuditableDocument{
	@Id
	private String roleid;
	private String role;

}
