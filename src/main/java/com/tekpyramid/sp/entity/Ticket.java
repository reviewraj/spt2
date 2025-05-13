package com.tekpyramid.sp.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tekpyramid.sp.enums.IssueType;
import com.tekpyramid.sp.enums.LicensePermission;
import com.tekpyramid.sp.enums.Priority;
import com.tekpyramid.sp.enums.Severity;
import com.tekpyramid.sp.enums.Status;
import com.tekpyramid.sp.utility.AuditableDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Document
@Data
@EqualsAndHashCode(callSuper = false)
public class Ticket extends AuditableDocument {
	@Id
	private String ticketId;
	@DBRef
	private TicketTemplate templateId;

	private IssueType issueType;

	private String summaryOfIssue;

	private String issueDescription;

	private Severity severity=Severity.MINOR;

	private Priority priority=Priority.LOW;
	@DBRef
	private LicenseName licenseNameId;
	private LicensePermission licensePermission;
	@DBRef
	private List<SupportingFile> supportingFiles = new ArrayList<>();
	@DBRef
	private List<Comment> comments;
	private Status status;
	@DBRef
	private Privilege assignGroup;
	@DBRef
	private User assignedTo;
	@DBRef
	private List<User> supportmembers;
    private Map<String, Object> customFieldValues = new HashMap<>();

}
