package com.tekpyramid.sp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tekpyramid.sp.entity.EmailConfig;

public interface EmailConfigRepository extends MongoRepository<EmailConfig,String> {

}
