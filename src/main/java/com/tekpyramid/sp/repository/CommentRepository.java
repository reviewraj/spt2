package com.tekpyramid.sp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tekpyramid.sp.entity.Comment;
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

}
