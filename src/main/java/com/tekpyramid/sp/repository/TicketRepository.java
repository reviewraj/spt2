package com.tekpyramid.sp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tekpyramid.sp.entity.Ticket;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {


}
