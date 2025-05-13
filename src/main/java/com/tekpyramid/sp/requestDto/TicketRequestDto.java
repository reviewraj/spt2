package com.tekpyramid.sp.requestDto;

import java.util.Map;

import com.tekpyramid.sp.enums.IssueType;
import com.tekpyramid.sp.enums.LicensePermission;
import com.tekpyramid.sp.enums.Status;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TicketRequestDto {

    @Parameter(description = "The username associated with the ticket")
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String userName;

    @Parameter(description = "The email address associated with the ticket")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
        message = "Email must be a valid format (e.g., user@example.com)"
    )
    private String email;

    @Parameter(description = "The type of issue (e.g., technical, service, etc.)")
    @NotNull(message = "Issue type ID cannot be null")
    private IssueType issueType;

    @Parameter(description = "A brief summary of the issue")
    @NotBlank(message = "The summary of issue must not be blank")
    private String summaryOfIssue;

    @Parameter(description = "A detailed description of the issue")
    @NotBlank(message = "The issue description must not be blank")
    private String issueDescription;


    @Parameter(description = "The name of the license associated with the issue")
    @NotNull(message = "License name ID cannot be null")
    private String licenseName;

    @Enumerated(EnumType.STRING)
    @Parameter(description = "The permission level for the license (e.g., READ, WRITE, ADMIN)")
    private LicensePermission licensePermission;

    // @Parameter(description = "List of comments associated with the ticket")
    // private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    @Parameter(description = "The current status of the ticket (e.g., OPEN, CLOSED, IN_PROGRESS)")
    private Status status = Status.OPEN;
    @Parameter(description = "The template ID used for dynamic custom fields")
    private String templateId;

    @Parameter(description = "Map of custom field values matching template labels")
    private Map<String, Object> customFieldValues;
    
}
