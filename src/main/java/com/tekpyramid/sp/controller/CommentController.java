package com.tekpyramid.sp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tekpyramid.sp.entity.Comment;
import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/supportDesk/comment/v1")
@Tag(name = "Comment Controller", description = "APIs related to Comment operations")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping(value = "/saveComment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Create Comment",
        description = "Creates a new comment"
    )
    public ResponseEntity<?> saveComment(
        @RequestPart(value = "comment") Comment comment
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComment(comment));
    }

    @DeleteMapping("/deleteComment")
    @Operation(
        summary = "Delete Comment",
        description = "Deletes the provided comment"
    )
    public ResponseEntity<?> deleteComment(@RequestBody Comment comment) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.delete(comment));
    }

    @PutMapping("/updateComment")
    @Operation(
        summary = "Update Comment",
        description = "Updates the provided comment"
    )
    public ResponseEntity<?> updateComment(@RequestBody Comment comment) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.update(comment));
    }

    @GetMapping("/getAll")
    @Operation(
        summary = "Get All Comments",
        description = "Retrieves all the comments"
    )
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAll());
    }

    @PostMapping("/{ticketId}/comments/{commentId}/replies")
    @Operation(
        summary = "Add Reply to Comment",
        description = "Adds a reply to a specific comment within a ticket"
    )
    public ResponseEntity<ResponseDto> addReply(
        @PathVariable String ticketId, 
        @PathVariable String commentId,
        @RequestPart(value = "file", required = false) List<MultipartFile> file,
        @RequestPart(value = "comment") Comment reply
    ) {
        return ResponseEntity.ok(commentService.saveReplyToComment(ticketId, commentId, file, reply));
    }
}
