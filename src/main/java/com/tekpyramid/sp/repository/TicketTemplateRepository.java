package com.tekpyramid.sp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tekpyramid.sp.entity.TicketTemplate;

public interface TicketTemplateRepository extends MongoRepository<TicketTemplate, String> {
	
}