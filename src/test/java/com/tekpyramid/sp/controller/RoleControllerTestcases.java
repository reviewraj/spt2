package com.tekpyramid.sp.controller;

import com.tekpyramid.sp.entity.Role;
import com.tekpyramid.sp.repository.RoleRepository;
import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.service.JwtService;
import com.tekpyramid.sp.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestParam;

import static org.mockito.Mockito.when;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RoleControllerTestcases {
    @MockBean
private RoleService roleService;
    @MockBean
   private RoleRepository roleRepository;
    @Autowired private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @Test
    public void addRoleTestCase() throws Exception {
        Role privilege = new Role();
        privilege.setRole("ROLE_ADMIN");
        ResponseDto responseDto = new ResponseDto(false, "Role created successfully", privilege);
        when(roleService.saveRole("ROLE_ADMIN")).thenReturn(responseDto);
        mockMvc.perform(post("/supportDesk/v1/admin/saveRole").param("roleName","ROLE_ADMIN"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Role created successfully"))
                .andExpect(jsonPath("$.error").value(false))
                ;

    }
}
