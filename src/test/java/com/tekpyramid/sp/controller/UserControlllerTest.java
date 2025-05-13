package com.tekpyramid.sp.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekpyramid.sp.notification.NotificationService;
import com.tekpyramid.sp.repository.EmailConfigRepository;
import com.tekpyramid.sp.requestDto.LoginRequestDto;
import com.tekpyramid.sp.requestDto.UserRequestDTO;
import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.responseDto.UserReponseDto;
import com.tekpyramid.sp.service.JwtService;
import com.tekpyramid.sp.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControlllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;
	@MockBean
	private JwtService jwtService;
	@MockBean
	private NotificationService notificationService;
	@MockBean
	private EmailConfigRepository emailConfigRepository;

	@Autowired
	private ObjectMapper objectMapper;


	@Test
	public void testAddUser() throws Exception {
		UserRequestDTO userRequestDTO = new UserRequestDTO("Nagaraju", "nagaraju7876482@gmail.com", "9348574434");
		ResponseDto responseDto = new ResponseDto(false, "User saved successfully",
				new UserReponseDto("Nagaraju", "nagaraju7876482@gmail.com"));
		when(userService.addUser(userRequestDTO)).thenReturn(responseDto);
		mockMvc.perform(post("/supportDesk/v1/save").contentType("application/json")
				.content(objectMapper.writeValueAsString(userRequestDTO)))
		        .andExpect(status().isCreated())
				.andExpect(jsonPath("$.message").value("User saved successfully"))
		.andExpect(jsonPath("$.error").value(false));
	}
	@Test
	public void loginTest() throws JsonProcessingException, Exception{
		LoginRequestDto loginRequestDto = new LoginRequestDto("nagaraju7876482@gmail.com","4df94r9348");
		when(userService.login(loginRequestDto)).thenReturn(ResponseEntity.ok("74930534y8t95y4thjehjhwe3e98u2893"));
		mockMvc.perform(post("/supportDesk/v1/login").contentType("application/json").content(objectMapper.writeValueAsString(loginRequestDto)))
		.andExpect(status().isOk())
		.andExpect(content().string(("74930534y8t95y4thjehjhwe3e98u2893")));
	}
	@Test
	public void unathorizedTest() throws JsonProcessingException, Exception {
		LoginRequestDto loginRequestDto = new LoginRequestDto("invalid@gmail.com","4df94r9348");
		when(userService.login(loginRequestDto)).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed"));
		mockMvc.perform(post("/supportDesk/v1/login").contentType("application/json").content(objectMapper.writeValueAsString(loginRequestDto)))
		.andExpect(status().isUnauthorized())
		.andExpect(content().string("Authentication failed"));
	}
	
	@Test
	public void updatingTest() throws JsonProcessingException, Exception {
		UserRequestDTO userRequestDTO = new UserRequestDTO("Nagaraju", "nagaraju7876482@gmail.com", "9348574434");
		UserReponseDto userReponseDto = new UserReponseDto(userRequestDTO.getUserName(), userRequestDTO.getEmail());
		ResponseDto responseDto = new ResponseDto(false, "user updated successfully", userReponseDto);
		when(userService.update(userRequestDTO)).thenReturn(responseDto);
		mockMvc.perform(put("/supportDesk/v1/update").contentType("application/json").content(objectMapper.writeValueAsString(userRequestDTO)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.error").value(false))
		.andExpect(jsonPath("$.message").value("user updated successfully"));
	}
	@Test
	public void updatingUnauthorizedTest() throws JsonProcessingException, Exception {
		UserRequestDTO userRequestDTO = new UserRequestDTO("Nagaraju", "nagaraju7876482@gmail.com", "9348574434");
		ResponseDto responseDto = new ResponseDto(true, "unauthorised access", null);
		when(userService.update(userRequestDTO)).thenReturn(responseDto);
		mockMvc.perform(put("/supportDesk/v1/update").contentType("application/json").content(objectMapper.writeValueAsString(userRequestDTO)))
		.andExpect(status().isUnauthorized())
		.andExpect(jsonPath("$.error").value(true))
		.andExpect(jsonPath("$.message").value("unauthorised access"));
	}

}
