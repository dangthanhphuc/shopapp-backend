package com.example.shopappbackend.services.comment;

import com.example.shopappbackend.dtos.CommentDTO;
import com.example.shopappbackend.entities.Comment;
import com.example.shopappbackend.entities.Product;
import com.example.shopappbackend.entities.User;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.CommentRepository;
import com.example.shopappbackend.services.product.IProductService;
import com.example.shopappbackend.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final IProductService productService;
    @Override
    public Comment createComment(CommentDTO commentDTO) throws DataNotFoundException {
        Product existingProduct = productService.getProductById(commentDTO.getProductId());
        User existingUser = userService.getUserById(commentDTO.getUserId());

        Comment newComment = Comment.builder()
                .content(commentDTO.getContent())
                .user(existingUser)
                .product(existingProduct)
                .build();


        return commentRepository.save(newComment);
    }

    @Override
    public Comment updateComment(Long id, CommentDTO commentDTO) throws DataNotFoundException {
        Product existingProduct = productService.getProductById(commentDTO.getProductId());
        User existingUser = userService.getUserById(commentDTO.getUserId());
        Comment existingComment = getCommentById(id);

        existingComment.setContent(commentDTO.getContent());

        return commentRepository.save(existingComment);
    }

    @Override
    public void deleteCommentById(Long id) throws DataNotFoundException {
        Comment existingComment = getCommentById(id);
        existingComment.setDeleted(true);
        commentRepository.save(existingComment);
    }

    @Override
    public List<Comment> getCommentsByProduct(Long productId) throws DataNotFoundException {
        Product existingProduct = productService.getProductById(productId);
        List<Comment> comments = commentRepository.findByProductId(productId).stream()
                .filter(comment -> !comment.isDeleted())
                .toList();
        return comments;
    }

    @Override
    public List<Comment> getCommentsByProductAndUser(Long productId, Long userId) throws DataNotFoundException {
        Product existingProduct = productService.getProductById(productId);
        User existingUser = userService.getUserById(userId);
        List<Comment> comments = commentRepository.findByProductIdAndUserId(productId, userId).stream()
                .filter(comment -> !comment.isDeleted())
                .toList();
        return comments;
    }

    public Comment getCommentById(Long id) throws DataNotFoundException {
        return commentRepository.findById(id).stream()
                .filter(comment -> !comment.isDeleted())
                .findFirst()
                .orElseThrow(
                        () -> new DataNotFoundException("Comment not found")
                );
    }
}
