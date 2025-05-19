// 댓글 작성용 DTO
package com.on_bapsang.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequest {
    private String content;
    private Long parentId; // 대댓글일 경우 부모 댓글 ID (없으면 null)
}
