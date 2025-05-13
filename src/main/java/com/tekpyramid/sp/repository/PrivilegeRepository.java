package com.tekpyramid.sp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tekpyramid.sp.entity.Privilege;
import java.util.List;


@Repository
public interface PrivilegeRepository extends MongoRepository<Privilege, String> {
	Optional<Privilege> findByPrivilegeId(String privilegeId);
	Optional<Privilege> findByPrivilege(String privilege);
	List<Privilege> findByPrivilegeRegexIgnoreCase(String regex);
}
