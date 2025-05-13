package com.tekpyramid.sp.responseDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

import com.tekpyramid.sp.entity.Comment;
import com.tekpyramid.sp.entity.LicenseName;
import com.tekpyramid.sp.entity.Privilege;
import com.tekpyramid.sp.entity.SupportingFile;
import com.tekpyramid.sp.enums.IssueType;
import com.tekpyramid.sp.enums.LicensePermission;
import com.tekpyramid.sp.enums.Priority;
import com.tekpyramid.sp.enums.Severity;
import com.tekpyramid.sp.enums.Status;

import lombok.Data;
@Data
public class TicketResponseDto {
	@Id
	private String ticketId;
	
	private IssueType issueType;
	
	private String summaryOfIssue;
	
	private String issueDescription;
	
	private Severity severity;
	
	private Priority priority;
	private LicenseName licenseNameId;
	
	private LicensePermission licensePermission;
	
	private List<SupportingFile> supportingFiles = new ArrayList<>();
	
	private List<Comment> comments;
	
	private Status status;
	
	private Privilege assignGroup;
	
	private UserReponseDto assignedTo;
	
	private List<UserReponseDto> supportmembers;
	
    private Date createdDate;
    
    private Date lastModifiedDate;
    
    private String createdBy;
    
    private String modifiedBy;
    
    private String templateId;
    
    private Map<String, Object> customFieldValues;
}
