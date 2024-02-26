package com.example.shopappbackend.services.comment;

import com.example.shopappbackend.dtos.CommentDTO;
import com.example.shopappbackend.entities.Comment;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICommentService {
    Comment createComment(CommentDTO commentDTO) throws DataNotFoundException;
    Comment updateComment(Long id, CommentDTO commentDTO) throws DataNotFoundException;
    void deleteCommentById(Long id) throws DataNotFoundException;
    List<Comment> getCommentsByProduct(Long productId) throws DataNotFoundException;

    List<Comment> getCommentsByProductAndUser(Long productId, Long userId) throws DataNotFoundException;
}
