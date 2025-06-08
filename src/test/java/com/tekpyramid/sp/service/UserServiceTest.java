package com.tekpyramid.sp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tekpyramid.sp.entity.EmailConfig;
import com.tekpyramid.sp.entity.Privilege;
import com.tekpyramid.sp.entity.Role;
import com.tekpyramid.sp.entity.User;
import com.tekpyramid.sp.notification.NotificationService;
import com.tekpyramid.sp.repository.EmailConfigRepository;
import com.tekpyramid.sp.repository.PrivilegeRepository;
import com.tekpyramid.sp.repository.RoleRepository;
import com.tekpyramid.sp.repository.UserRepository;
import com.tekpyramid.sp.requestDto.UserRequestDTO;
import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.responseDto.UserReponseDto;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PrivilegeRepository privilegeRepository;
    @Mock private EmailConfigRepository emailConfigRepository;
    @Mock private NotificationService notificationService;
    @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testAddUser_SuccessfulCreation() {
        UserRequestDTO requestDTO = new UserRequestDTO("Nagaraju", "nagaraju@example.com", "password123");

        Role mockRole = new Role();
        mockRole.setRole("ROLE_CUSTOMER");
        lenient(). when(roleRepository.findByRole("ROLE_CUSTOMER")).thenReturn(Optional.of(mockRole));

        Privilege mockPrivilege = new Privilege();
        mockPrivilege.setPrivilege("ROLE_CUSTOMER");
        lenient().when(privilegeRepository.findByPrivilege("ROLE_CUSTOMER")).thenReturn(Optional.of(mockPrivilege));

        EmailConfig emailConfig = new EmailConfig();
        emailConfig.setEnabled(false); 
        lenient().when(emailConfigRepository.findById("6800f14bc050687bc31bf268")).thenReturn(Optional.of(emailConfig));

        lenient().when(bCryptPasswordEncoder.encode("password123")).thenReturn("encodedPassword");

        
       
        User savedUser = new User();
        savedUser.setEmail("nagaraju@example.com");
        savedUser.setUserName("Nagaraju");
        savedUser.setRole(mockRole);
        savedUser.setPrivilege(mockPrivilege);
        lenient().when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());
        lenient().when(userRepository.save(any(User.class))).thenReturn(savedUser);

        ResponseDto response = userService.addUser(requestDTO);
System.out.println(response);
        assertEquals("usercreatedsuccessfully", response.getMessage());
        UserReponseDto data = (UserReponseDto) response.getData();
        System.out.println(data);
       assertEquals("Nagaraju", data.getUserName());
       assertEquals("ROLE_CUSTOMER", data.getRole());
        assertEquals("ROLE_CUSTOMER", data.getPrivilege());
    }
}
