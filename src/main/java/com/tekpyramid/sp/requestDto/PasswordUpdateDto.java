package com.tekpyramid.sp.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.Parameter;

@Data
public class PasswordUpdateDto {

    @Parameter(description = "The new password for the user. It must be at least 8 characters long.")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Parameter(description = "The confirmation of the new password. It must match the new password.")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String renterpassword;
}
