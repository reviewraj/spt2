package com.tekpyramid.sp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tekpyramid.sp.entity.Comment;
import com.tekpyramid.sp.entity.EmailConfig;
import com.tekpyramid.sp.entity.SupportingFile;
import com.tekpyramid.sp.entity.Ticket;
import com.tekpyramid.sp.exeception.DataNotFound;
import com.tekpyramid.sp.notification.NotificationService;
import com.tekpyramid.sp.repository.CommentRepository;
import com.tekpyramid.sp.repository.EmailConfigRepository;
import com.tekpyramid.sp.repository.TicketRepository;
import com.tekpyramid.sp.responseDto.CommentResponseDto;
import com.tekpyramid.sp.responseDto.ResponseDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
	
	private final CommentRepository commentRepository;
	
	private final SupportFileService supportFileService;
	
	private final TicketRepository ticketRepository;
	
	 
	private final ModelMapper modelMapper;
	
	private final NotificationService notificationService;
	 
	private final EmailConfigRepository emailConfigRepository;

	@Override
	public ResponseDto delete(Comment commentToDelete) {
		Optional<Comment> optionalComment = commentRepository.findById(commentToDelete.getCommentId());

		if (optionalComment.isEmpty()) {
			throw new DataNotFound("Comment not found for deletion");
		}

		List<Comment> allComments = commentRepository.findAll();
		for (Comment parent : allComments) {
			if (removeReferenceFromParent(parent.getReplies(), commentToDelete.getCommentId())) {
				commentRepository.save(parent);
				break;
			}
		}

		List<Ticket> allTickets = ticketRepository.findAll();
		for (Ticket ticket : allTickets) {
			List<Comment> comments = ticket.getComments();
			if (comments != null && comments.removeIf(c -> c.getCommentId().equals(commentToDelete.getCommentId()))) {
				ticketRepository.save(ticket);
			}
		}

		commentRepository.deleteById(commentToDelete.getCommentId());

		return new ResponseDto(false, "Comment deleted successfully", null);
	}

	private boolean removeReferenceFromParent(List<Comment> replies, String commentId) {
		if (replies == null)
			return false;

		Iterator<Comment> iterator = replies.iterator();
		while (iterator.hasNext()) {
			Comment reply = iterator.next();
			if (reply.getCommentId().equals(commentId)) {
				iterator.remove();
				return true;
			}
			if (removeReferenceFromParent(reply.getReplies(), commentId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Comment update(Comment updatedComment) {
		List<Comment> allComments = commentRepository.findAll();

		for (Comment parent : allComments) {
			if (updateNestedCommentRecursive(parent, updatedComment)) {
				return commentRepository.save(parent);
			}
		}

		Comment existing = commentRepository.findById(updatedComment.getCommentId())
				.orElseThrow(() -> new DataNotFound("Comment not found"));

		existing.setMessage(updatedComment.getMessage());
		existing.setAuthor(updatedComment.getAuthor());
		existing.setType(updatedComment.getType());
		existing.setTimestamp(updatedComment.getTimestamp());

		return commentRepository.save(existing);
	}

	private boolean updateNestedCommentRecursive(Comment parent, Comment updatedComment) {
		if (parent.getReplies() == null)
			return false;

		for (Comment reply : parent.getReplies()) {
			if (reply.getCommentId().equals(updatedComment.getCommentId())) {
				reply.setMessage(updatedComment.getMessage());
				reply.setAuthor(updatedComment.getAuthor());
				reply.setType(updatedComment.getType());
				reply.setTimestamp(updatedComment.getTimestamp());
				return true;
			}
			if (updateNestedCommentRecursive(reply, updatedComment)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public List<Comment> getAll() {
		return commentRepository.findAll();
	}

	@Override
	public Comment saveComment(Comment comment) {
		return commentRepository.save(comment);
	}

//	@Override
//	public Comment saveReplyToComment(String ticketId, String commentId, List<MultipartFile> supportingFiles,
//			Comment reply) {
//		Comment comment = commentRepository.findById(commentId).orElseThrow();
//		try {
//			List<Comment> replies = comment.getReplies();
//			List<SupportingFile> supportingFiles2 = reply.getSupportingFiles();
//			if (replies == null)
//				replies = new ArrayList<>();
//			for (MultipartFile file : supportingFiles) {
//				SupportingFile supportingFile = new SupportingFile();
//				supportingFile.setData(file.getBytes());
//				supportingFile.setFileType(file.getContentType());
//				ResponseDto saveFile = supportFileService.saveFile(supportingFile);
//				supportingFiles2.add((SupportingFile) saveFile.getData());
//			}
//			reply.setSupportingFiles(supportingFiles2);
//			Comment save = commentRepository.save(reply);
//			replies.add(save);
//			return commentRepository.save(comment);
//		} catch (IOException e) {
//		}
//		return comment;
//	}
	@Override
	public ResponseDto saveReplyToComment(String ticketId, String commentId, List<MultipartFile> supportingFiles, Comment reply) {
	    Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

	    try {
	    	System.out.println(supportingFiles);
	        List<SupportingFile> replyFiles = new ArrayList<>();
	        long size_limit=14680064l;
			int files_limit=5;
			if(supportingFiles!=null) {
	        for (MultipartFile file : supportingFiles) {
				if(size_limit-file.getSize()>0&&files_limit>0){
				SupportingFile supportingFile = new SupportingFile();
				System.out.println("file size :"+file.getSize());
				supportingFile.setData(file.getBytes());
				supportingFile.setFileType(file.getContentType());
				size_limit-=file.getSize();
				files_limit--;
				ResponseDto saveFile = supportFileService.saveFile(supportingFile);
				replyFiles.add((SupportingFile) saveFile.getData());
			}
				}
	        }
	        reply.setSupportingFiles(replyFiles);
Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()-> new DataNotFound("ticket not found with id :"+ticketId) );
	        Comment savedReply = commentRepository.save(reply);
			Optional<EmailConfig> byId = emailConfigRepository.findById("6800f173c050687bc31bf26a");
			EmailConfig emailConfig = byId.get();
			if(emailConfig.isEnabled()) {
				CompletableFuture.runAsync(()->
				notificationService.commentNotification(ticket, savedReply, savedReply.getCreatedBy()));
				}
	        List<Comment> replies = comment.getReplies();
	        if (replies == null) replies = new ArrayList<>();
	        replies.add(savedReply);
	        comment.setReplies(replies);
System.out.println(savedReply.getReplies());
	        return new ResponseDto(false, "comment created successfully", modelMapper.map( commentRepository.save(comment),CommentResponseDto.class));
	    } catch (IOException e) {
	        throw new RuntimeException("Error while processing supporting files", e);
	    }
	}

}
