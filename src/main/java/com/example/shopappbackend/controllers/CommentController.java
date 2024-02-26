package com.example.shopappbackend.controllers;

import com.example.shopappbackend.dtos.CommentDTO;
import com.example.shopappbackend.entities.Comment;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.responses.comment.CommentResponse;
import com.example.shopappbackend.services.comment.ICommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/comments")
public class CommentController {
    private final ICommentService commentService;
    @PostMapping("")
    public ResponseEntity<?> createComment(
            @Valid @RequestBody CommentDTO commentDTO,
            BindingResult result
    ){
        try {
            if(result.hasErrors()){
                List<String> errors = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errors);
            }
            Comment newComment = commentService.createComment(commentDTO);
            return ResponseEntity.ok().body(CommentResponse.fromComment(newComment));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateComment(
            @PathVariable("id") Long commentId,
            @Valid @RequestBody CommentDTO commentDTO,
            BindingResult result
    ){
        try {
            if(result.hasErrors()){
                List<String> errors = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errors);
            }
            Comment updatedComment = commentService.updateComment(commentId, commentDTO);
            return ResponseEntity.ok().body(CommentResponse.fromComment(updatedComment));
        } catch (DataNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long commentId){
        try {
            commentService.deleteCommentById(commentId);
            return ResponseEntity.ok().body("Deleted comment successfully");
        } catch (DataNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllComments(
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam(value = "product_id") Long productId
    ){
        List<Comment> comments;
        try{
            if (userId == null){
                comments = commentService.getCommentsByProduct(productId);
            } else {
                comments = commentService.getCommentsByProductAndUser(productId, userId);
            }
            return ResponseEntity.ok().body(comments.stream().map(CommentResponse::fromComment).toList());
        } catch (DataNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
