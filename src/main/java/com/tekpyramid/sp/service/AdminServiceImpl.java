package com.tekpyramid.sp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tekpyramid.sp.entity.Privilege;
import com.tekpyramid.sp.entity.Role;
import com.tekpyramid.sp.entity.Ticket;
import com.tekpyramid.sp.entity.User;
import com.tekpyramid.sp.exeception.DataNotFound;
import com.tekpyramid.sp.exeception.UserNotExist;
import com.tekpyramid.sp.repository.PrivilegeRepository;
import com.tekpyramid.sp.repository.RoleRepository;
import com.tekpyramid.sp.repository.TicketRepository;
import com.tekpyramid.sp.repository.UserRepository;
import com.tekpyramid.sp.requestDto.UserRequestDTO;
import com.tekpyramid.sp.responseDto.PrivilegeResponseDto;
import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.responseDto.TicketResponseDto;
import com.tekpyramid.sp.responseDto.UserReponseDto;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	private final TicketRepository ticketRepository;
	
	private final UserRepository userRepository;
	
	private final ModelMapper modelMapper;
	
	private  final PrivilegeRepository privilegeRepository;
	
	private final RoleRepository roleRepository;
	

	@Override
	public ResponseDto assigntickettoUsers(String ticketId, UserRequestDTO users) {
		Ticket ticket=ticketRepository.findById(ticketId).orElseThrow(()->new DataNotFound("ticket not found with the id :"+ticketId));
	  if(users==null)
		  return new ResponseDto(true, "please select the users before assigning tickets", null);
		  User orElseThrow = userRepository.findByUserid(users.getEmail()).orElseThrow(()->new UserNotExist("user is not found with id "+users.getEmail()));
	  ticket.setAssignedTo(orElseThrow);
	  Ticket save = ticketRepository.save(ticket);
	  TicketResponseDto ticketResponseDto = modelMapper.map(save, TicketResponseDto.class);
	   UserReponseDto userReponseDto = modelMapper.map(save.getAssignedTo(),UserReponseDto.class);
	   ticketResponseDto.setAssignedTo(userReponseDto);
	  return new ResponseDto(false, "ticket is assigned to particular users", ticketResponseDto);
	}

	@Override
	public ResponseDto assignPrivilgetoUsers(String privilgeId, List<UserRequestDTO> users) {
		
		Privilege privilege = privilegeRepository.findByPrivilegeId(privilgeId).orElseThrow(()->new DataNotFound("privilege not found with id : "+privilgeId));
		List<UserReponseDto> faultuserList = new ArrayList<>();
		List<UserReponseDto> userResponseDtos = new ArrayList<>();
		for(UserRequestDTO user:users) {
			Optional<User> byUserid = userRepository.findByEmail(user.getEmail());
			if(byUserid.isEmpty())	{
				faultuserList.add(modelMapper.map(user, UserReponseDto.class));
			continue;}
			User user2 = byUserid.get();
			user2.setPrivilege(privilege);
			User save = userRepository.save(user2);
			UserReponseDto userReponseDto = modelMapper.map(save, UserReponseDto.class);
			userResponseDtos.add(userReponseDto);
			}
		if(faultuserList.isEmpty())
			return new ResponseDto(false, "privilge is assigned to the particular users ", userResponseDtos);
		else {
			return new ResponseDto(false, "privilege is not assigned for the particular user because they are not active users", faultuserList);
		}
		}
		@Override
		public ResponseDto assignRoletoUsers(String roleId, List<UserRequestDTO> users) {
			
			Role role  = roleRepository.findByRoleid(roleId).orElseThrow(()->new DataNotFound("role not found with id : "+roleId));
	
			List<UserReponseDto> faultuserList = new ArrayList<>();
			List<UserReponseDto> userResponseDtos = new ArrayList<>();
			for(UserRequestDTO user:users) {
				Optional<User> byUserid = userRepository.findByEmail(user.getEmail());
				if(byUserid.isEmpty())	{faultuserList.add(modelMapper.map(user, UserReponseDto.class));
				continue;}
				User user2 = byUserid.get();
				user2.setRole(role);
				User save = userRepository.save(user2);
				UserReponseDto userReponseDto = modelMapper.map(save, UserReponseDto.class);
				userResponseDtos.add(userReponseDto);
				}
			if(faultuserList.isEmpty())
				return new ResponseDto(false, "role is assigned to the particular users ", userResponseDtos);
			else {
				return new ResponseDto(false, "role is not assigned for the particular user because they are not active users", faultuserList);
			}
	}

		@Override
		public ResponseDto getAllUserswithtickets(int page, int size, String sortBy, String sortDir) {
			Sort sort= sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
			Pageable pageable = PageRequest.of(page, size, sort);
			Page<User> all = userRepository.findAll(pageable);
			List<User> listOfUsers = all.getContent();
			List<UserReponseDto> userReponseDtos =new ArrayList<>();
			for(User user:listOfUsers) {
				UserReponseDto userReponseDto = modelMapper.map(user, UserReponseDto.class);
				userReponseDto.setNoOfTickets(ticketRepository.findAll().stream().
filter(ticket->ticket.getAssignedTo()!=null).filter(tic->tic.getAssignedTo().getEmail().equalsIgnoreCase(user.getEmail())).count());
		userReponseDtos.add(userReponseDto);
			}
			return new ResponseDto(false, "user with tickets listed below", userReponseDtos);	
			}

		@Override
		public ResponseDto getAllUserswithprivilege( int page, int size, String sortBy, String sortDir) {
			Sort sort= sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
			Pageable pageable = PageRequest.of(page, size, sort);
			Page<User> all = userRepository.findAll(pageable);
			List<User> listOfUsers = all.getContent();
			List<Privilege> privilegeList = privilegeRepository.findAll();
			List<PrivilegeResponseDto> privilegeResponseDtos = new ArrayList<>();
			for(Privilege privilege:privilegeList) {
				PrivilegeResponseDto privilegeResponseDto = modelMapper.map(privilege, PrivilegeResponseDto.class);
				privilegeResponseDto.setNumberOfUsers(listOfUsers.stream().filter(user -> user.getPrivilege().getPrivilege().equalsIgnoreCase(privilege.getPrivilege())).count());
				privilegeResponseDtos.add(privilegeResponseDto);
			}
			if(!privilegeResponseDtos.isEmpty())
			return new ResponseDto(false, "user with tickets listed below", privilegeResponseDtos);	
			return new ResponseDto(false, "there is no users", null);	
		}

		@Override
		public ResponseDto assignSupportToTicket(String ticketId, List<UserRequestDTO> users) {
			Ticket ticket  = ticketRepository.findById(ticketId).orElseThrow(()->new DataNotFound("ticket not found with id : "+ticketId));
			List<UserReponseDto> faultuserList = new ArrayList<>();
			List<User> supportmembers = ticket.getSupportmembers();
			if(supportmembers==null)supportmembers= new ArrayList<>();
			for(UserRequestDTO user:users) {
				Optional<User> byUserid = userRepository.findByEmail(user.getEmail());
				if(byUserid.isEmpty())	{faultuserList.add(modelMapper.map(user, UserReponseDto.class));
				continue;
				}
				 supportmembers.add(byUserid.get());
				}
			ticket.setSupportmembers(supportmembers);
			Ticket save = ticketRepository.save(ticket);
			TicketResponseDto ticketResponseDto = modelMapper.map(save, TicketResponseDto.class);
			if(faultuserList.isEmpty())
				return new ResponseDto(false, "role is assigned to the particular users ", ticketResponseDto);
			else {
				return new ResponseDto(false, "role is not assigned for the particular user because they are not active users", faultuserList);
			}

		}

	

}
