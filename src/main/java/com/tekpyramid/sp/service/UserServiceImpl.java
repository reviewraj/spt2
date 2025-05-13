package com.tekpyramid.sp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tekpyramid.sp.entity.Privilege;
import com.tekpyramid.sp.entity.Role;
import com.tekpyramid.sp.entity.User;
import com.tekpyramid.sp.enums.Status;
import com.tekpyramid.sp.exeception.PasswordMismatch;
import com.tekpyramid.sp.exeception.UserAlreadyExist;
import com.tekpyramid.sp.exeception.UserNotExist;
import com.tekpyramid.sp.notification.NotificationService;
import com.tekpyramid.sp.repository.EmailConfigRepository;
import com.tekpyramid.sp.repository.PrivilegeRepository;
import com.tekpyramid.sp.repository.RoleRepository;
import com.tekpyramid.sp.repository.UserRepository;
import com.tekpyramid.sp.requestDto.LoginRequestDto;
import com.tekpyramid.sp.requestDto.PasswordUpdateDto;
import com.tekpyramid.sp.requestDto.UserRequestDTO;
import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.responseDto.UserReponseDto;
import com.tekpyramid.sp.utility.RepeatedValues;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder(12);
	private  final PrivilegeRepository privilegeRepository;
	
	private  final RoleRepository roleRepository;
	
    private final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;
	
	private final NotificationService notificationService;
	
	private final EmailConfigRepository emailConfigRepository;
	

	@Override
	public ResponseDto addUser(UserRequestDTO userRequestDTO) {
		Optional<User> optionalUser = userRepository.findByEmail(userRequestDTO.getEmail());
		if (optionalUser.isEmpty()) {
			User user = new User();
			BeanUtils.copyProperties(userRequestDTO, user);
			Role role = roleRepository.findByRole("ROLE_CUSTOMER").get();
			user.setRole(role);
			Privilege privilege = privilegeRepository.findByPrivilege("ROLE_CUSTOMER").get();
			user.setPrivilege(privilege);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setCreatedBy(user.getEmail());
			User save = userRepository.save(user);
				try {
					if(emailConfigRepository.findById("6800f14bc050687bc31bf268").get().isEnabled())
					CompletableFuture.runAsync(() -> notificationService.forgotPassword(save.getEmail()))
				    .exceptionally(ex -> {
				        log.error("Mail send failed: " + ex.getMessage());
				        return null;
				    });

				} catch (Exception exeception) {
					log.error("the issue is "+exeception.getMessage());

				}
			UserReponseDto userReponseDto = new UserReponseDto();
			BeanUtils.copyProperties(save, userReponseDto);
			userReponseDto.setRole(save.getRole().getRole());
			userReponseDto.setPrivilege(save.getPrivilege().getPrivilege());
			return new ResponseDto(false, "usercreatedsuccessfully", userReponseDto);
		}
		throw new UserAlreadyExist("user is already exist with email :" + userRequestDTO.getEmail());
	}

	public ResponseDto update(UserRequestDTO userRequestDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = authentication.getName();
		if(!name.equalsIgnoreCase(userRequestDTO.getEmail()))
		return new ResponseDto(true, "unauthorised access", null);
		Optional<User> optionalUser = userRepository.findByEmail(userRequestDTO.getEmail());
		if (optionalUser.isPresent() && optionalUser.get().getStatus() == Status.ACTIVE) {
			User user = optionalUser.get();
			BeanUtils.copyProperties(userRequestDTO, user);
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			User save = userRepository.save(user);
			UserReponseDto userReponseDto = new UserReponseDto();
			BeanUtils.copyProperties(save, userReponseDto);
			userReponseDto.setRole(save.getRole().getRole());
			userReponseDto.setPrivilege(save.getPrivilege().getPrivilege());
			return new ResponseDto(false, "user updated successfully", userReponseDto);
		}
		throw new UserNotExist("user is not exist with email :" + userRequestDTO.getEmail());
	}

	@Override
	public ResponseEntity<String> login(LoginRequestDto loginRequestDto) {
		Optional<User> optionalUser = userRepository.findByEmail(loginRequestDto.getEmail());
		if (optionalUser.isEmpty() || optionalUser.get().getStatus() != Status.ACTIVE)
			throw new UserNotExist(RepeatedValues.USERNOtEXIST);
		User user = optionalUser.get();
		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), loginRequestDto.getPassword()));
		if (authenticate.isAuthenticated()) {
			 String token = jwtService.generateToken(user.getEmail());
		        HttpHeaders headers = new HttpHeaders();
		        headers.add("Authorization", "Bearer " + token);
		        return ResponseEntity.ok()
		                             .headers(headers)
		                             .body("Authentication successful"); 
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");

	}

	

	@Override
	public ResponseDto delete(String id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isEmpty() || optionalUser.get().getStatus() == Status.INACTIVE)
			throw new UserNotExist(RepeatedValues.USERNOtEXIST);

		User user = optionalUser.get();
		user.setStatus(Status.INACTIVE);
		userRepository.save(user);
		return new ResponseDto(false, "user deleted successfully", user.getEmail());

	}

	@Override
	public ResponseDto reset(String email, PasswordUpdateDto passwordUpdateDto) {
		if(!passwordUpdateDto.getPassword().equals(passwordUpdateDto.getRenterpassword()))
			throw  new PasswordMismatch("user credentials mismatch");
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isEmpty() || optionalUser.get().getStatus() == Status.INACTIVE)
			throw new UserNotExist(RepeatedValues.USERNOtEXIST);
		User user = optionalUser.get();
		user.setPassword(bCryptPasswordEncoder.encode( passwordUpdateDto.getPassword()));
		user.setStatus(Status.ACTIVE);
		User save = userRepository.save(user);
		UserReponseDto userReponseDto = new UserReponseDto();
		BeanUtils.copyProperties(save, userReponseDto);
		userReponseDto.setRole(save.getRole().getRole());
		userReponseDto.setPrivilege(save.getPrivilege().getPrivilege());
		return new ResponseDto(false, "Credentials updated successfully", userReponseDto);
	}

	@Override
	public ResponseDto getAll(int page, int size, String sortBy, String sortDir, String inputUserSearch,
			String inputPrivilegeSearch, String inputRoleSearch,String userId) {
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(page, size, sort);
		String userSearch = inputUserSearch == null||inputUserSearch.isBlank() ? ".*" :".*" +  inputUserSearch + ".*";
		String privilegeSearch = inputPrivilegeSearch == null||inputUserSearch.isBlank() ? ".*" :".*" +  inputPrivilegeSearch + ".*";
		String roleSearch = inputRoleSearch == null ||inputUserSearch.isBlank()? ".*" : ".*" + inputRoleSearch + ".*";
		 List<Privilege> privileges = privilegeRepository.findByPrivilegeRegexIgnoreCase(privilegeSearch);
		    List<Role> roles = roleRepository.findByRoleRegexIgnoreCase(roleSearch);

		    if (privileges.isEmpty() || roles.isEmpty()) {
		        return new ResponseDto(false, "No users match the given criteria", Collections.emptyList());
		    }
		Page<User> byUserNameContainingIgnoreCase = userRepository.findByUserNameRegexIgnoreCaseAndPrivilegeInAndRoleInAndStatus(userSearch, privileges,
				roles,"ACTIVE", pageable);
		List<User> userList = byUserNameContainingIgnoreCase.getContent();
		List<UserReponseDto> userResponse = new ArrayList<>();
		for (User us : userList) {
				UserReponseDto userReponseDto = new UserReponseDto();
				BeanUtils.copyProperties(us, userReponseDto);
				userReponseDto.setRole(us.getRole().getRole());
				userReponseDto.setPrivilege(us.getPrivilege().getPrivilege());
				userResponse.add(userReponseDto);
		}if(userId!=null)
		 userResponse = userResponse.stream().filter(userRespo-> userRespo.getUserid().equalsIgnoreCase(userId)).toList();
		if (userList.isEmpty()) {
			return new ResponseDto(false, "there is no users", null);
		}
		return new ResponseDto(false, "users are listed below", userResponse);

	}
	
}