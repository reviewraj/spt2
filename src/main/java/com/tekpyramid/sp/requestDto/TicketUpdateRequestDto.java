package com.tekpyramid.sp.requestDto;

import java.util.Map;

import com.tekpyramid.sp.enums.IssueType;
import com.tekpyramid.sp.enums.LicensePermission;
import com.tekpyramid.sp.enums.Priority;
import com.tekpyramid.sp.enums.Severity;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class TicketUpdateRequestDto {

    @Parameter(description = "The username associated with the ticket")
    private String userName;

    @Parameter(description = "The email address associated with the ticket")
    private String email;

    @Enumerated(EnumType.STRING)
    @Parameter(description = "The type of issue (e.g., technical, service, etc.)")
    private IssueType issueType;

    @Parameter(description = "A brief summary of the issue")
    private String summaryOfIssue;

    @Parameter(description = "A detailed description of the issue")
    private String issueDescription;

    @Enumerated(EnumType.STRING)
    @Parameter(description = "The severity level of the issue (e.g., Low, Medium, High)")
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Parameter(description = "The priority level of the issue (e.g., Low, Medium, High)")
    private Priority priority;

    @Parameter(description = "The name of the license associated with the issue")
    private String licenseName;

    @Enumerated(EnumType.STRING)
    @Parameter(description = "The permission level for the license (e.g., READ, WRITE, ADMIN)")
    private LicensePermission licensePermission;
    
    private String templateId;

    private Map<String, Object> customFieldValues;

}
