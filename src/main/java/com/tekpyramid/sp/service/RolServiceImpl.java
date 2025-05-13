package com.tekpyramid.sp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tekpyramid.sp.entity.Role;
import com.tekpyramid.sp.repository.RoleRepository;
import com.tekpyramid.sp.responseDto.ResponseDto;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class RolServiceImpl implements RoleService {
   
	private final RoleRepository roleRepository;
	@Override
	public ResponseDto saveRole(String roleName) {
		try {
			Role privilege = new Role();
			privilege.setRole(roleName);
			Role save = roleRepository.save(privilege);
			return new ResponseDto(false, "Role created successfully", save);
		} catch (Exception e) {
			return new ResponseDto(true, e.getMessage(), null);
		}
	}

	@Override
	public ResponseDto updateRole(String roleId, String roleName) {
		try {
			Optional<Role> byPrivilegeid = roleRepository.findByRoleid(roleId);
			Role privilege = byPrivilegeid.get();
			privilege.setRole(roleName);
			Role save = roleRepository.save(privilege);
			return new ResponseDto(false, "role update successfully", save);
			} catch (Exception e) {
				return new ResponseDto(true, e.getMessage(), null);
			}
	}

	@Override
	public ResponseDto deletePrivilege(String roleId) {
		try {
			Optional<Role> byPrivilegeid = roleRepository.findByRoleid(roleId);
			Role privilege = byPrivilegeid.get();
			roleRepository.delete(privilege);
			return new ResponseDto(false, "role deleted successfully", privilege);
		} catch (Exception e) {
			return new ResponseDto(true, e.getMessage(), null);
		}
	}

	@Override
	public ResponseDto getAllPrivilege() {
		List<Role> roles=roleRepository.findAll();
		return new ResponseDto(true, "role listed successfully", roles);
	}

}
