package com.tekpyramid.sp.service;

import java.util.List;

import com.tekpyramid.sp.requestDto.UserRequestDTO;
import com.tekpyramid.sp.responseDto.ResponseDto;

public interface AdminService {

	ResponseDto assigntickettoUsers(String ticketId, UserRequestDTO users);

	ResponseDto assignPrivilgetoUsers(String privilgeId, List<UserRequestDTO> users);

	ResponseDto assignRoletoUsers(String roleId, List<UserRequestDTO> users);

	ResponseDto getAllUserswithtickets(int page, int size, String sortBy, String sortDir);

	ResponseDto getAllUserswithprivilege(int page, int size, String sortBy, String sortDir);

	ResponseDto assignSupportToTicket(String ticketId, List<UserRequestDTO> users);
	
}