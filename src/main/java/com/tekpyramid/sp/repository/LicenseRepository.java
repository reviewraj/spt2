package com.tekpyramid.sp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tekpyramid.sp.entity.LicenseName;

public interface LicenseRepository extends MongoRepository<LicenseName,String> {

}
