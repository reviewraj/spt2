package com.tekpyramid.sp.service;

import org.springframework.stereotype.Service;

import com.tekpyramid.sp.responseDto.ResponseDto;

@Service
public interface RoleService {

	ResponseDto saveRole(String roleName);

	ResponseDto updateRole(String roleId, String roleName);

	ResponseDto deletePrivilege(String roleId);

	ResponseDto getAllPrivilege();

}
