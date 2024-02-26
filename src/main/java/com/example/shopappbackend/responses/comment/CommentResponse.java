package com.example.shopappbackend.responses.comment;

import com.example.shopappbackend.entities.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    @JsonProperty("content")
    private String content;
    @JsonProperty("username")
    private String username;
    @JsonProperty("role_id")
    private Long roleId;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static CommentResponse fromComment(Comment comment){
        return CommentResponse.builder()
                .content(comment.getContent())
                .username(comment.getUser().getFullname())
                .roleId(comment.getUser().getRole().getId())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
