package com.tekpyramid.sp.responseDto;

import java.util.List;

import com.tekpyramid.sp.entity.Comment;
import com.tekpyramid.sp.entity.SupportingFile;
import com.tekpyramid.sp.enums.CommentType;

import lombok.Data;

@Data
public class CommentResponseDto {
	private String commentId;
    private String author;
    private String message;
    private String timestamp;
    private CommentType type;
	private List<SupportingFile> supportingFiles ;
    private List<Comment> replies ; 

}
