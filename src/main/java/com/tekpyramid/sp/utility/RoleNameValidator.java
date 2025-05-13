package com.tekpyramid.sp.utility;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tekpyramid.sp.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class RoleNameValidator implements ConstraintValidator<ValidRoleName, String> {

    @Autowired
    private RoleRepository roleRepository;
    private Set<String> cachedRoleNames;
    @PostConstruct
    public void loadRoles() {
        cachedRoleNames = roleRepository.findAll()
                .stream()
                .map(role->role.getRole())
                .collect(Collectors.toSet());
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
    	return cachedRoleNames.contains(value);
    }
}

