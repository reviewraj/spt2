package com.tekpyramid.sp.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.tekpyramid.sp.enums.Priority;
import com.tekpyramid.sp.enums.Severity;
import com.tekpyramid.sp.enums.Status;
import com.tekpyramid.sp.requestDto.CommentRequestDto;
import com.tekpyramid.sp.requestDto.TicketRequestDto;
import com.tekpyramid.sp.requestDto.TicketUpdateRequestDto;
import com.tekpyramid.sp.responseDto.ResponseDto;

public interface TicketService {

	ResponseDto createTicket(List<MultipartFile> file, TicketRequestDto ticketRequestDto);

	ResponseDto deleteTicket(String ticketId);

	ResponseDto updateTicket(List<MultipartFile> file, String ticketId, TicketUpdateRequestDto ticketUpdateRequestDto);

	ResponseDto saveReplyToTicket(String ticketId, List<MultipartFile> file, CommentRequestDto comment);
	ResponseDto getAll(Integer page, Integer size, String sortBy, String sortDir, String search, String ticketId,
			Severity severity, Priority priority, String createdBy, Status status, Pageable pageable);

}
