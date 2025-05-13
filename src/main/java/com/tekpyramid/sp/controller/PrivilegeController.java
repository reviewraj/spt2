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
import com.tekpyramid.sp.service.PrivilegeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/supportDesk/v1/admin")
@Tag(name = "Privilege Controller", description = "APIs related to privilege operations")
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @PostMapping("/savePrivilege")
    @Operation(
        summary = "Create Privilege",
        description = "Creates a new privilege by providing the privilege name"
    )
    public ResponseEntity<ResponseDto> addPrivilege(@RequestParam String privilegeName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(privilegeService.savePrivilege(privilegeName));
    }

    @PutMapping("/updatePrivilege")
    @Operation(
        summary = "Update Privilege",
        description = "Updates an existing privilege with the provided privilege ID and new privilege name"
    )
    public ResponseEntity<ResponseDto> updatePrivilege(
        @RequestParam String privilegeId,
        @RequestParam String privilegeName
    ) {
        return ResponseEntity.ok().body(privilegeService.updatePrivilege(privilegeId, privilegeName));
    }

    @DeleteMapping("/deletePrivilege")
    @Operation(
        summary = "Delete Privilege",
        description = "Deletes the privilege based on the provided privilege ID"
    )
    public ResponseEntity<ResponseDto> deletePrivilege(@RequestParam String privilegeId) {
        return ResponseEntity.ok().body(privilegeService.deletePrivilege(privilegeId));
    }

    @GetMapping("/getAllPrivilege")
    @Operation(
        summary = "Get All Privileges",
        description = "Retrieves all the privileges available"
    )
    public ResponseEntity<ResponseDto> getAllPrivilege() {
        return ResponseEntity.ok().body(privilegeService.getAllPrivilege());
    }
}
