package com.on_bapsang.backend.dto.mypage;

import java.time.LocalDateTime;

public class MyPost {

    private Long postId;
    private String title;
    private String content;
    private String imageUrl;
    private int scrapCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private Double x;
    private Double y;

    public MyPost(Long postId, String title, String content, String imageUrl,
                  int scrapCount, int commentCount, LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.scrapCount = scrapCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.x = x;
        this.y = y;
    }

}
