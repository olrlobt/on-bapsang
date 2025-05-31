// 댓글 응답용 DTO
package com.on_bapsang.backend.dto;

import com.on_bapsang.backend.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponse {
    private final Long id;
    private final String content;
    private final String nickname;
    private final String profileImage;
    private final LocalDateTime createdAt;
    private final List<CommentResponse> children;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getUser().getNickname();
        this.profileImage = comment.getUser().getProfileImage();
        this.createdAt = comment.getCreatedAt();
        this.children = comment.getChildren().stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }
}
