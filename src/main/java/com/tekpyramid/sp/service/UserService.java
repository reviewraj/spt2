package com.tekpyramid.sp.service;

import org.springframework.http.ResponseEntity;

import com.tekpyramid.sp.requestDto.LoginRequestDto;
import com.tekpyramid.sp.requestDto.PasswordUpdateDto;
import com.tekpyramid.sp.requestDto.UserRequestDTO;
import com.tekpyramid.sp.responseDto.ResponseDto;

public interface UserService {
	public ResponseDto addUser(UserRequestDTO userRequestDTO);

	public ResponseEntity<String> login(LoginRequestDto loginRequestDto);

	public ResponseDto update(UserRequestDTO UserRequestDTO);

	public ResponseDto delete(String id);


	public ResponseDto reset(String email, PasswordUpdateDto passwordUpdateDto);

//	public ResponseDto getAll(int page, int size, String sortBy, String sortDir,String usersearch, String privileagesearch, String rolesearch);

	ResponseDto getAll(int page, int size, String sortBy, String sortDir, String inputUserSearch, String userId,
			String inputPrivilegeSearch, String inputRoleSearch);
}
