package com.tekpyramid.sp.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @Parameter(description = "The user's email address. It must be in a valid email format (e.g., user@example.com).")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
    message = "Email must be a valid format (e.g., user@example.com)")
    private String email;

    @Parameter(description = "The user's password. It must be at least 8 characters long.")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
