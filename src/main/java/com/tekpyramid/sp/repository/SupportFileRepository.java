package com.tekpyramid.sp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tekpyramid.sp.entity.SupportingFile;
@Repository
public interface SupportFileRepository extends MongoRepository<SupportingFile,String> {

}
