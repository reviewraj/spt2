package com.tekpyramid.sp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tekpyramid.sp.entity.Role;


@Repository
public interface RoleRepository extends MongoRepository<Role,String> {
 Optional<Role> findByRoleid(String roleid);
 Optional<Role> findByRole(String role);
 List<Role> findByRoleRegexIgnoreCase(String regex);
}
