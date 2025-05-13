package com.tekpyramid.sp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tekpyramid.sp.entity.Privilege;
import com.tekpyramid.sp.repository.PrivilegeRepository;
import com.tekpyramid.sp.responseDto.ResponseDto;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {
	
	private final PrivilegeRepository privilegeRepository;

	@Override
	public ResponseDto savePrivilege(String privilegeName) {
		try {
			Privilege privilege = new Privilege();
			privilege.setPrivilege(privilegeName);
			Privilege save = privilegeRepository.save(privilege);
			return new ResponseDto(true, "privilege saved successfully", save);
		} catch (Exception e) {
			return new ResponseDto(true, e.getMessage(), null);
		}
	}

	@Override
	public ResponseDto updatePrivilege(String privilegeId, String privilegeName) {
		try {
		Optional<Privilege> byPrivilegeid = privilegeRepository.findByPrivilegeId(privilegeId);
		Privilege privilege = byPrivilegeid.get();
		privilege.setPrivilege(privilegeName);
		Privilege save = privilegeRepository.save(privilege);
		return new ResponseDto(true, "privilege saved successfully", save);
		} catch (Exception e) {
			return new ResponseDto(true, e.getMessage(), null);
		}
	}

	@Override
	public ResponseDto deletePrivilege(String privilegeId) {
		try {
		Optional<Privilege> byPrivilegeid = privilegeRepository.findByPrivilegeId(privilegeId);
		Privilege privilege = byPrivilegeid.get();
		privilegeRepository.delete(privilege);
		return new ResponseDto(true, "privilege deleted successfully", privilege);
	} catch (Exception e) {
		return new ResponseDto(true, e.getMessage(), null);
	}
	}

	@Override
	public ResponseDto getAllPrivilege() {
		try {
		List<Privilege> all = privilegeRepository.findAll();
		return new ResponseDto(true, "privilege listed successfully", all);
	} catch (Exception e) {
		return new ResponseDto(true, e.getMessage(), null);
	}
	}

}
