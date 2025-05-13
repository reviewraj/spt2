package com.tekpyramid.sp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tekpyramid.sp.entity.Privilege;
import com.tekpyramid.sp.entity.Role;
import com.tekpyramid.sp.entity.User;



@Repository
public interface UserRepository extends MongoRepository<User, String>{
   Optional<User> findByUserid(String userid);
   Optional<User> findByEmail(String email);

   Page<User> findByUserNameRegexIgnoreCaseAndPrivilegeInAndRoleInAndStatus(
		    String userNameRegex, List<Privilege> privileges, List<Role> roles, String status, Pageable pageable);

}
