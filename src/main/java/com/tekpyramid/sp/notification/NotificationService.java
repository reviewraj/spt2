package com.tekpyramid.sp.notification;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.tekpyramid.sp.entity.Comment;
import com.tekpyramid.sp.entity.Ticket;
import com.tekpyramid.sp.entity.User;
import com.tekpyramid.sp.enums.Status;
import com.tekpyramid.sp.exeception.InValidEmail;
import com.tekpyramid.sp.exeception.UserNotExist;
import com.tekpyramid.sp.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Component
public class NotificationService {
	
	private final UserRepository userRepository;
	
	private final JavaMailSender mailSender;
	
	private final SpringTemplateEngine templateEngine;
	@Value("${app.base.url}")
	private String baseUrl;
	@Async
	public void forgotPassword(String email) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isEmpty() || optionalUser.get().getStatus() == Status.INACTIVE)
			throw new UserNotExist("user does not exist");

		try {
			User user = optionalUser.get();
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setTo(email);
			helper.setSubject("Password Generation/Updation");
			Context context = new Context();
			context.setVariable("username", user.getUserName());
			context.setVariable("resetLink", "reset");
			String html = templateEngine.process("reset-password", context);
			helper.setText(html, true);
			mailSender.send(message);
		} catch (Exception exception) {
			throw new InValidEmail("invalid user email: " + email + " - " + exception.getMessage());
		}

	}@Async
	public void commentNotification(Ticket ticket, Comment comment, String toEmail) {
	    Optional<User> optionalUser = userRepository.findByEmail(toEmail);
	    if (optionalUser.isEmpty() || optionalUser.get().getStatus() == Status.INACTIVE) {
	        throw new UserNotExist("user does not exist");
	    }
	    
	    try {
	    	
	    	if (ticket == null || comment == null) {
	            throw new IllegalArgumentException("Ticket and comment cannot be null");
	        }

	        List<String> validRecipients = new ArrayList<>();
	        
	        if (ticket.getCreatedBy() != null && !ticket.getCreatedBy().trim().isEmpty()) {
	            validRecipients.add(ticket.getCreatedBy());
	        }
	        
	        if (comment.getCreatedBy() != null && !comment.getCreatedBy().trim().isEmpty()) {
	            validRecipients.add(comment.getCreatedBy());
	        }
	        
	        if (ticket.getAssignedTo() != null && ticket.getAssignedTo().getEmail() != null 
	            && !ticket.getAssignedTo().getEmail().trim().isEmpty()) {
	            validRecipients.add(ticket.getAssignedTo().getEmail());
	        }

	        if (toEmail != null && !toEmail.trim().isEmpty()) {
	            validRecipients.add(toEmail);
	        }

	        if (validRecipients.isEmpty()) {
	            System.out.println("No valid recipients found - mail not sent");
	            return;
	        }

	        String[] recipientsArray = validRecipients.toArray(new String[0]);
	        User user = optionalUser.get();
	        
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	        helper.setTo(recipientsArray);
	        
	        String filePath = "D:\\sts-workspace\\spt2\\New Text Document.txt";
	        File fileToAttach = new File(filePath);
	        
	        if (!fileToAttach.exists()) {
	            throw new IOException("Attachment file not found at: " + filePath);
	        }
	        if (!fileToAttach.canRead()) {
	            throw new IOException("Cannot read attachment file. Check permissions for: " + filePath);
	        }
	        
	        helper.addAttachment(fileToAttach.getName(), 
	                          new FileSystemResource(fileToAttach),
	                          "text/plain"); 
	        helper.setSubject("New comment on your ticket #" + ticket.getTicketId());
	        
	        Context context = new Context();
	        context.setVariable("username", user.getUserName());
	        context.setVariable("ticketId", ticket.getTicketId());
	        context.setVariable("issue", ticket.getIssueType());
	        context.setVariable("status", ticket.getStatus());
	        context.setVariable("comment", comment.getMessage());
	        
	        String html = templateEngine.process("comment", context);
	        helper.setText(html, true);
	        
	        mailSender.send(message);
	        
	    } catch (MessagingException e) {
	        throw new InValidEmail("Failed to send email: " + e.getMessage());
	    } catch (IOException e) {
	        throw new InValidEmail("Attachment error: " + e.getMessage());
	    } catch (Exception e) {
	        throw new InValidEmail("Unexpected error: " + e.getMessage());
	    }
	}   @Async
	    public void sendTicketCreationNotification(Ticket ticket) {
	        try {
	            MimeMessage message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	            
	            helper.setTo(ticket.getCreatedBy());
	            
	            helper.setSubject("New Ticket Created #" + ticket.getTicketId());
	            
	            Context context = new Context();
	            context.setVariable("ticket", ticket);
	            context.setVariable("baseUrl", baseUrl);
	            
	            String html = templateEngine.process("ticketcreation", context);
	            helper.setText(html, true);
	            
	            mailSender.send(message);
	        } catch (Exception e) {
	            System.err.println("Failed to send ticket creation notification: " + e.getMessage());
	        }
	    }
	    
	    @Async
	    public void sendStatusChangeNotification(Ticket ticket, Status previousStatus, 
	                                          String updatedBy, String statusComment) {
	        try {
	            MimeMessage message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	            
	            helper.setTo(ticket.getCreatedBy());
	            if (ticket.getAssignedTo() != null) {
	                helper.addCc(ticket.getAssignedTo().getEmail());
	            }
	            
	            helper.setSubject("Ticket #" + ticket.getTicketId() + " status changed to " + ticket.getStatus());
	            
	            Context context = new Context();
	            context.setVariable("ticket", ticket);
	            context.setVariable("previousStatus", previousStatus);
	            context.setVariable("updatedBy", updatedBy);
	            context.setVariable("statusComment", statusComment);
	            context.setVariable("baseUrl", baseUrl);
	            
	            String html = templateEngine.process("email/ticket-status-change", context);
	            helper.setText(html, true);
	            
	            mailSender.send(message);
	        } catch (Exception e) {
	            System.err.println("Failed to send status change notification: " + e.getMessage());
	        }
	    }
	    @Async
	    public void sendAssignmentNotification(Ticket ticket, User previousAssignee, 
	                                        String updatedBy, String assignmentComment,
	                                        boolean isNewAssignee) {
	        try {
	            MimeMessage message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	            
	            List<String> toRecipients = new ArrayList<>();
	            if (isNewAssignee && ticket.getAssignedTo() != null) {
	                toRecipients.add(ticket.getAssignedTo().getEmail());
	            }
	            if (ticket.getCreatedBy() != null) {
	                toRecipients.add(ticket.getCreatedBy());
	            }
	            
	            helper.setTo(toRecipients.toArray(new String[0]));
	            
	            if (previousAssignee != null && !isNewAssignee) {
	                helper.addCc(previousAssignee.getEmail());
	            }
	            
	            helper.setSubject("Ticket #" + ticket.getTicketId() + " assignment updated");
	            
	            Context context = new Context();
	            context.setVariable("ticket", ticket);
	            context.setVariable("previousAssignee", previousAssignee);
	            context.setVariable("updatedBy", updatedBy);
	            context.setVariable("assignmentComment", assignmentComment);
	            context.setVariable("baseUrl", baseUrl);
	            context.setVariable("isNewAssignee", isNewAssignee);
	            context.setVariable("recipientName", isNewAssignee ? 
	                ticket.getAssignedTo().getUserName() : ticket.getCreatedBy());
	            
	            String html = templateEngine.process("email/ticket-assignment-change", context);
	            helper.setText(html, true);
	            
	            mailSender.send(message);
	        } catch (Exception e) {
	            System.err.println("Failed to send assignment notification: " + e.getMessage());
	        }
	    }
		public NotificationService(UserRepository userRepository, JavaMailSender mailSender,
				SpringTemplateEngine templateEngine) {
			super();
			this.userRepository = userRepository;
			this.mailSender = mailSender;
			this.templateEngine = templateEngine;
		}
	}