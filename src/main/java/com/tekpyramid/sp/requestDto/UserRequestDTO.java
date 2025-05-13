package com.tekpyramid.sp.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.Parameter;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Parameter(description = "The username of the user (must be between 3 and 50 characters)")
    private String userName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
        message = "Email must be a valid format (e.g., user@example.com)")
    @Parameter(description = "The email address of the user (must be a valid format)")
    private String email;
    
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @Parameter(description = "The phone number of the user (must be 10 digits)")
    private String phoneNo;

	public UserRequestDTO(
			@NotBlank(message = "Username cannot be empty") @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") String userName,
			@NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email format") @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Email must be a valid format (e.g., user@example.com)") String email,
			@NotBlank(message = "Phone number cannot be empty") @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits") String phoneNo) {
		super();
		this.userName = userName;
		this.email = email;
		this.phoneNo = phoneNo;
	}

	public UserRequestDTO() {
		super();
	}
    
}
