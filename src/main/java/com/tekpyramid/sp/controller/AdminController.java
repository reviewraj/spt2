package com.tekpyramid.sp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tekpyramid.sp.requestDto.UserRequestDTO;
import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.service.AdminService;

@RestController
@RequestMapping("/supportDesk/v1/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;
	@PostMapping("/assignTicketToUsers/{ticketId}")
	public ResponseEntity<ResponseDto> assigntickettoUsers(@PathVariable("ticketId") String ticketId,@RequestBody UserRequestDTO users){
		return ResponseEntity.ok().body(adminService.assigntickettoUsers(ticketId,users));
		
	}
	@PostMapping("/assignPrivilgeToUsers/{privilgeId}")
	public ResponseEntity<ResponseDto> assignPrivilgetoUsers(@PathVariable("privilgeId") String privilgeId,@RequestBody List<UserRequestDTO> users){
		return ResponseEntity.ok().body(adminService.assignPrivilgetoUsers(privilgeId,users));
		
	}
	@PostMapping("/assignRoleToUsers/{roleId}")
	public ResponseEntity<ResponseDto> assignRoleToUsers(@PathVariable("roleId") String roleId,@RequestBody List<UserRequestDTO> users){
		return ResponseEntity.ok().body(adminService.assignRoletoUsers(roleId,users));
		
	}
	@GetMapping("/getAllUsersWithTickets")
	public ResponseEntity<ResponseDto> getAllUserswithtickets(
			@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "userName") String sortBy,
	        @RequestParam(defaultValue = "asc") String sortDir
	        ){

		return ResponseEntity.ok().body(adminService.getAllUserswithtickets(page,size,sortBy,sortDir));
	}
	@GetMapping("/getAllUserswithprivilege")
	public ResponseEntity<ResponseDto> getAllUserswithprivilege(
			@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "userName") String sortBy,
	        @RequestParam(defaultValue = "asc") String sortDir	
			){
		return ResponseEntity.ok().body(adminService.getAllUserswithprivilege(page,size,sortBy,sortDir));
	}
	@PostMapping("/assignSupportToTicket/{ticketId}")
	public ResponseEntity<ResponseDto> assignSupportToTicket(@PathVariable("ticketId") String ticketId,@RequestBody List<UserRequestDTO> users){
		return ResponseEntity.ok().body(adminService.assignSupportToTicket(ticketId,users));
		
	}
	
}
