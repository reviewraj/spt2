package com.tekpyramid.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/supportDesk/v1/admin")
@Tag(name = "Role Controller", description = "APIs related to role operations")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/saveRole")
    @Operation(
        summary = "Create Role",
        description = "Creates a new role by providing the role name"
    )
    public ResponseEntity<ResponseDto> addRole(@RequestParam String roleName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.saveRole(roleName));
    }

    @PutMapping("/updateRole")
    @Operation(
        summary = "Update Role",
        description = "Updates an existing role with the provided role ID and new role name"
    )
    public ResponseEntity<ResponseDto> updateRole(
        @RequestParam String roleId,
        @RequestParam String roleName
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(roleId, roleName));
    }

    @DeleteMapping("/deleteRole")
    @Operation(
        summary = "Delete Role",
        description = "Deletes the role based on the provided role ID"
    )
    public ResponseEntity<ResponseDto> deleteRole(@RequestParam String roleId) {
        return ResponseEntity.ok().body(roleService.deletePrivilege(roleId));
    }

    @GetMapping("/getAllRoles")
    @Operation(
        summary = "Get All Roles",
        description = "Retrieves all the roles available"
    )
    public ResponseEntity<ResponseDto> getAllRoles() {
        return ResponseEntity.ok().body(roleService.getAllPrivilege());
    }
}
