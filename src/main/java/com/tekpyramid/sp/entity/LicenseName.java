package com.tekpyramid.sp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tekpyramid.sp.utility.AuditableDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Document 
@Data
@EqualsAndHashCode(callSuper = false)
public class LicenseName extends AuditableDocument{
	@Id
	private String licenseId;
	private String licenseName;
	
}
