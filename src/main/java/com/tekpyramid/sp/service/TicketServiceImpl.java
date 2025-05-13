package com.tekpyramid.sp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tekpyramid.sp.entity.Comment;
import com.tekpyramid.sp.entity.EmailConfig;
import com.tekpyramid.sp.entity.LicenseName;
import com.tekpyramid.sp.entity.SupportingFile;
import com.tekpyramid.sp.entity.Ticket;
import com.tekpyramid.sp.entity.User;
import com.tekpyramid.sp.enums.CommentType;
import com.tekpyramid.sp.enums.Priority;
import com.tekpyramid.sp.enums.Severity;
import com.tekpyramid.sp.enums.Status;
import com.tekpyramid.sp.exeception.DataNotFound;
import com.tekpyramid.sp.notification.NotificationService;
import com.tekpyramid.sp.repository.EmailConfigRepository;
import com.tekpyramid.sp.repository.TicketRepository;
import com.tekpyramid.sp.repository.UserRepository;
import com.tekpyramid.sp.requestDto.CommentRequestDto;
import com.tekpyramid.sp.requestDto.TicketRequestDto;
import com.tekpyramid.sp.requestDto.TicketUpdateRequestDto;
import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.responseDto.TicketResponseDto;
import com.tekpyramid.sp.responseDto.UserReponseDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {
	
	private final TicketRepository ticketRepository;
	private final ModelMapper modelMapper;
	private final LicenseService licenseService;
	private final SupportFileService supportFileService;
	private final CommentService commentService;
	private final MongoTemplate mongoTemplate;
	private final NotificationService notificationService;
	private final EmailConfigRepository emailConfigRepository;
	private final UserRepository userRepository;

	@Override
	public ResponseDto createTicket(List<MultipartFile> supportingFiles, TicketRequestDto ticketRequestDto) {
		try {
			Ticket ticket = modelMapper.map(ticketRequestDto, Ticket.class);
			ResponseDto saveLicense = licenseService.saveLicense(ticketRequestDto.getLicenseName());
			ticket.setLicenseNameId((LicenseName) saveLicense.getData());
			List<SupportingFile> supportingFileList = ticket.getSupportingFiles();
			long size_limit = 14680064l;
			int files_limit = 5;
			if (!(supportingFiles == null)) {
				for (MultipartFile file : supportingFiles) {
					if (size_limit - file.getSize() > 0 && files_limit > 0) {
						SupportingFile supportingFile = new SupportingFile();
						System.out.println("file size :" + file.getSize());
						supportingFile.setData(file.getBytes());
						supportingFile.setFileType(file.getContentType());
						size_limit -= file.getSize();
						files_limit--;
						ResponseDto saveFile = supportFileService.saveFile(supportingFile);
						supportingFileList.add((SupportingFile) saveFile.getData());
					}
				}
			}

			Ticket save = ticketRepository.save(ticket);
//			kafkaTemplate.send("ticker-created", new ResponseDto(false, "ticket-create", save));
			TicketResponseDto ticketResponseDto = modelMapper.map(save, TicketResponseDto.class);
			if (supportingFiles != null) {
				if (supportingFiles.size() == save.getSupportingFiles().size()) {
					return new ResponseDto(false, "ticket saved successfully", ticketResponseDto);
				} else {
					return new ResponseDto(false,
							"ticket saved successfully but your docs exceeded the limit of number docs / size please upload again",
							ticketResponseDto);
				}
			} else
				return new ResponseDto(false, "ticket saved successfully", ticketResponseDto);

		} catch (Exception e) {
			return new ResponseDto(true, e.getMessage(), null);
		}
	}

	@Override
	public ResponseDto deleteTicket(String ticketId) {
		Optional<Ticket> optional = ticketRepository.findById(ticketId);
		if (optional.isEmpty())
			throw new DataNotFound("ticket not found with id : " + ticketId);
		Ticket ticket = optional.get();
		ticketRepository.delete(ticket);
		return new ResponseDto(false, "ticket is deleted successfully", null);
	}

	@Override
	public ResponseDto getAll(Integer page, Integer size, String sortBy, String sortDir, String search,
			String ticketId,
		    Severity severity,
		    Priority priority,
		    String createdBy,
		    Status status,
		    Pageable pageable) {
			List<Criteria> criteriaList = new ArrayList<>();

    if (search != null && !search.isEmpty()) {
        criteriaList.add(new Criteria().orOperator(
            Criteria.where("summaryOfIssue").regex(search, "i"),
            Criteria.where("issueDescription").regex(search, "i")
        ));
    }
    if(ticketId!=null) {
    	criteriaList.add(Criteria.where("ticketId").is(ticketId));
    }
    if(createdBy!=null) {
    	criteriaList.add(Criteria.where("createdBy").is(createdBy));
    }

    if (severity != null) {
        criteriaList.add(Criteria.where("severity").is(severity));
    }

    if (priority != null) {
        criteriaList.add(Criteria.where("priority").is(priority));
    }

    if (status != null) {
        criteriaList.add(Criteria.where("status").is(status));
    }

    Criteria finalCriteria = new Criteria();
    if (!criteriaList.isEmpty()) {
        finalCriteria.andOperator(criteriaList.toArray(new Criteria[0]));
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String name = authentication.getName();
    Optional<User> byEmail = userRepository.findByEmail(name);
    User user = byEmail.get();
    Query query = new Query(finalCriteria).with(pageable);

    List<Ticket> tickets = mongoTemplate.find(query, Ticket.class);
    long count = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Ticket.class);

    PageImpl<Ticket> pgPageImpl= new PageImpl<Ticket>(tickets, pageable, count);
    List<Ticket> content = pgPageImpl.getContent();
    List<TicketResponseDto> ticketResponseDtos = new ArrayList<>();
    if(content!=null)
    for(Ticket ticket:content) {
    	TicketResponseDto ticketResponseDto = modelMapper.map(ticket,TicketResponseDto.class);
    	log.info("the comment before {}",ticketResponseDto.getComments());
    	if(user.getRole().getRole().equalsIgnoreCase("ROLE_CUSTOMER")) {
    		List<Comment> list = ticketResponseDto.getComments().stream().filter(comment->comment.getType()==CommentType.PUBLIC).toList();
    		ticketResponseDto.setComments(list);	
    	}
    	log.info("the comment after {}",ticketResponseDto.getComments());
    	if(ticket.getAssignedTo()!=null)
    	ticketResponseDto.setAssignedTo(modelMapper.map(ticket.getAssignedTo(), UserReponseDto.class));
    	ticketResponseDtos.add(ticketResponseDto);
    }
    return new ResponseDto(false,"tickets listed below",ticketResponseDtos );
	}

	
	@Override
	public ResponseDto updateTicket(List<MultipartFile> supportingFiles, String ticketId,
			TicketUpdateRequestDto ticketUpdateRequestDto) {
		try {
			Optional<Ticket> byId = ticketRepository.findById(ticketId);
			if (byId.isEmpty())
				throw new DataNotFound("ticket data not found");
			Ticket ticket = byId.get();
			modelMapper.map(ticketUpdateRequestDto, ticket);
			Ticket save = ticketRepository.save(ticket);
			long size_limit = 14680064l;
			int files_limit = 5;
			List<SupportingFile> supportingFiles2 = new ArrayList<>();
			if (!(supportingFiles == null)) {
				for (MultipartFile file : supportingFiles) {
					if (size_limit - file.getSize() > 0 && files_limit > 0) {
						SupportingFile supportingFile = new SupportingFile();
						System.out.println("file size :" + file.getSize());
						supportingFile.setData(file.getBytes());
						supportingFile.setFileType(file.getContentType());
						size_limit -= file.getSize();
						files_limit--;
						ResponseDto saveFile = supportFileService.saveFile(supportingFile);
						supportingFiles2.add((SupportingFile) saveFile.getData());
					}
				}
			}
			save.setSupportingFiles(supportingFiles2);
			Ticket save2 = ticketRepository.save(save);
			TicketResponseDto ticketResponseDto = modelMapper.map(save2, TicketResponseDto.class);
			return new ResponseDto(false, "ticket updated success fully", ticketResponseDto);
		} catch (Exception e) {
			return new ResponseDto(true, e.getMessage(), null);
		}

	}

	@Override
	public ResponseDto saveReplyToTicket(String ticketId, List<MultipartFile> supportingFiles, CommentRequestDto comment) {
		try {
			Ticket ticket = ticketRepository.findById(ticketId)
					.orElseThrow(() -> new DataNotFound("the ticket with id not found :" + ticketId));
			if (ticket.getComments() == null) {
				ticket.setComments(new ArrayList<>());
			}
			List<SupportingFile> supportingFiles2 = comment.getSupportingFiles();
			if (supportingFiles2 == null)
				supportingFiles2 = new ArrayList<>();
			long size_limit = 14680064l;
			int files_limit = 5;
			if (supportingFiles != null) {
				for (MultipartFile file : supportingFiles) {
					if (size_limit - file.getSize() > 0 && files_limit > 0) {
						SupportingFile supportingFile = new SupportingFile();
						supportingFile.setData(file.getBytes());
						supportingFile.setFileType(file.getContentType());
						size_limit -= file.getSize();
						files_limit--;
						ResponseDto saveFile = supportFileService.saveFile(supportingFile);
						supportingFiles2.add((SupportingFile) saveFile.getData());
					}
				}
			}
			
			comment.setSupportingFiles(supportingFiles2);
			Comment comment2 = modelMapper.map(comment, Comment.class);
			Comment saveComment = commentService.saveComment(comment2);
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			ticket.getComments().add(saveComment);
			Ticket save = ticketRepository.save(ticket);
			Optional<EmailConfig> byId = emailConfigRepository.findById("6800f173c050687bc31bf26a");
			EmailConfig emailConfig = byId.get();
			if (emailConfig.isEnabled()) {
				CompletableFuture.runAsync(() -> notificationService.commentNotification(ticket, saveComment, name));}
			TicketResponseDto ticketResponseDto = modelMapper.map(save, TicketResponseDto.class);
			return new ResponseDto(false, "comment add succesfully", ticketResponseDto);
		} catch (Exception e) {
			return new ResponseDto(true, e.getMessage(), null);
		}

	}

}

