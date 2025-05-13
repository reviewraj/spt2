package com.tekpyramid.sp.service;

import org.springframework.stereotype.Service;

import com.tekpyramid.sp.responseDto.ResponseDto;

@Service
public interface PrivilegeService {

	ResponseDto savePrivilege(String privilegeName);

	ResponseDto updatePrivilege(String privilegeId, String privilegeName);

	ResponseDto deletePrivilege(String privilegeId);

	ResponseDto getAllPrivilege();

}
