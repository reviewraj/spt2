package com.tekpyramid.sp.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tekpyramid.sp.entity.Comment;
import com.tekpyramid.sp.responseDto.ResponseDto;

public interface CommentService {
	Comment saveComment(Comment comment);

	ResponseDto delete(Comment comment);

	Comment update(Comment comment);

	List<Comment> getAll();

	ResponseDto saveReplyToComment(String ticketId, String commentId, List<MultipartFile> file, Comment reply);
}
