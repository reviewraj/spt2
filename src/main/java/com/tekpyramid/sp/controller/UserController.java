package com.tekpyramid.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tekpyramid.sp.notification.NotificationService;
import com.tekpyramid.sp.requestDto.LoginRequestDto;
import com.tekpyramid.sp.requestDto.PasswordUpdateDto;
import com.tekpyramid.sp.requestDto.UserRequestDTO;
import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/supportDesk/v1")
@Tag(name = "User Controller", description = "APIs related to user operations")
@Slf4j
public class UserController {

	
	@Autowired
	private UserService userService;
	@Autowired
	private NotificationService notificationService;

	@PostMapping("/save")
	 @Operation(summary = "saves the user", description = "saves the user and retur saved user json")
	public ResponseEntity<ResponseDto> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userRequestDTO));
	}

	@PostMapping("/login")
	 @Operation(summary = "Login User", description = "Authenticates user and returns JWT token")
	public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
		return userService.login(loginRequestDto);
	}

	@PutMapping("/update")
	@Operation(summary = "Update User", description = "Updates existing user details")
	public ResponseEntity<ResponseDto> update(@Valid @RequestBody UserRequestDTO userRequestDTO) {
		ResponseDto responseDto = userService.update(userRequestDTO);
		if(responseDto.isError())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
		return ResponseEntity.ok(responseDto);
	}
	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Delete User", description = "Deletes a user based on the provided ID")
	public ResponseEntity<ResponseDto> update(@PathVariable("id") String id) {
		return ResponseEntity.ok(userService.delete(id));
	}


	@PostMapping("/forgotPassword")
	@Operation(summary = "Forgot Password", description = "Sends a password reset link or OTP to the user's email")
	public void forgotPassword(@RequestParam String email) {
		notificationService.forgotPassword(email);

	}

	@PostMapping("/passwordreset/{email}")
	@Operation(summary = "Forgot Password", description = "Sends a password reset link or OTP to the user's email")
	public ResponseEntity<ResponseDto> forgotPassword(@PathVariable("email") String email,
			@Valid @RequestBody PasswordUpdateDto passwordUpdateDto) {
		return ResponseEntity.ok(userService.reset(email, passwordUpdateDto));

	}

	@GetMapping("/getAll")
	 @Operation(
		        summary = "Get All Users",
		        description = "Retrieves all users with pagination, sorting, and optional search filters"
		    )
	public ResponseEntity<ResponseDto> getAllUsers(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "userName") String sortBy,
	        @RequestParam(defaultValue = "asc") String sortDir,
	        @RequestParam(required = false) String userSearch,
	        @RequestParam(required = false) String privileageSearch,
	        @RequestParam(required = false) String roleSearch,
	        @RequestParam(required = false) String userId)
	{
				return ResponseEntity.ok(userService.getAll(page, size,sortBy,sortDir,userSearch,privileageSearch,roleSearch,userId));

}
}