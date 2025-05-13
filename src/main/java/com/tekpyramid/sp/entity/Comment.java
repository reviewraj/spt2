package com.tekpyramid.sp.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tekpyramid.sp.enums.CommentType;
import com.tekpyramid.sp.utility.AuditableDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@Document
@EqualsAndHashCode(callSuper = false)
public class Comment extends AuditableDocument{
	@Id
private String commentId;
    private String author;
    private String message;
    private String timestamp;
    private CommentType type;
    @DBRef
	private List<SupportingFile> supportingFiles ;
    @DBRef
    private List<Comment> replies ; 


    public void addReply(Comment reply) {
        this.setType(this.type);
        this.replies.add(reply);
    }
}
