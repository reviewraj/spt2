package com.tekpyramid.sp.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tekpyramid.sp.entity.TicketTemplate;
import com.tekpyramid.sp.enums.Priority;
import com.tekpyramid.sp.enums.Severity;
import com.tekpyramid.sp.enums.Status;
import com.tekpyramid.sp.repository.TicketTemplateRepository;
import com.tekpyramid.sp.requestDto.CommentRequestDto;
import com.tekpyramid.sp.requestDto.TicketRequestDto;
import com.tekpyramid.sp.requestDto.TicketUpdateRequestDto;
import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/supportDesk/ticket/v1")
@Tag(name = "Ticket Controller", description = "APIs related to ticket operations")
@AllArgsConstructor
public class TicketController {
    private final TicketService ticketService;
	private final TicketTemplateRepository templateRepository;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Create New Ticket",
        description = "Creates a new ticket and optionally uploads files associated with the ticket"
    )
    public ResponseEntity<ResponseDto> createTicket(
        @RequestPart(name = "file", required = false) List<MultipartFile> file,
        @RequestPart("ticket") TicketRequestDto ticketRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(file, ticketRequestDto));
    }

    @PutMapping("/update/{ticketId}")
    @Operation(
        summary = "Update Existing Ticket",
        description = "Updates an existing ticket with the given ticket ID and optionally uploads files"
    )
    public ResponseEntity<ResponseDto> updateTicket(
        @RequestPart(name = "file", required = false) List<MultipartFile> file,
        @PathVariable String ticketId,
        @RequestPart TicketUpdateRequestDto ticketUpdateRequestDto) {
        return ResponseEntity.ok().body(ticketService.updateTicket(file, ticketId, ticketUpdateRequestDto));
    }

    @DeleteMapping("/delete/{ticketId}")
    @Operation(
        summary = "Delete Ticket",
        description = "Deletes the ticket based on the provided ticket ID"
    )
    public ResponseEntity<ResponseDto> deleteTicket(@PathVariable("ticketId") String ticketId) {
        return ResponseEntity.ok().body(ticketService.deleteTicket(ticketId));
    }

    @GetMapping("/getAll")
    @Operation(
        summary = "Get All Tickets",
        description = "Retrieves all tickets with pagination, sorting, and optional filtering by severity, priority, and status"
    )
    public ResponseEntity<ResponseDto> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "lastModifiedDate") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String usersearch,
        @RequestParam(required = false) String ticketId,
        @RequestParam(required = false) Severity severity,
        @RequestParam(required = false) Priority priority,
        @RequestParam(required = false) String createdBy,
        @RequestParam(required = false) Status status
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok().body(ticketService.getAll(page, size, sortBy, sortDir, usersearch,ticketId, severity, priority,createdBy, status, pageable));
    }

    @PostMapping("/{ticketId}/comments")
    @Operation(
        summary = "Add Comment to Ticket",
        description = "Adds a comment to the ticket and optionally uploads files as part of the comment"
    )
    public ResponseEntity<ResponseDto> addComment(
        @PathVariable String ticketId,
        @RequestPart(value = "file", required = false) List<MultipartFile> file,
        @RequestPart(value = "comment") CommentRequestDto comment
    ) {
        return ResponseEntity.ok(ticketService.saveReplyToTicket(ticketId, file, comment));
    }
    @PostMapping("/template")
    public TicketTemplate createTemplate(@RequestBody TicketTemplate template) {
        return templateRepository.save(template);
    }

    @GetMapping("/template")
    public List<TicketTemplate> getTemplates() {
        return templateRepository.findAll();
    }
}
