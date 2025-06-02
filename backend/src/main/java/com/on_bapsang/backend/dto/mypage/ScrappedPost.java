package com.on_bapsang.backend.dto.mypage;

import java.time.LocalDateTime;

public class ScrappedPost {

    private Long postId;
    private String title;
    private String imageUrl;
    private int scrapCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private Double x;
    private Double y;

    public ScrappedPost(Long postId, String title, String imageUrl,
                        int scrapCount, int commentCount, LocalDateTime createdAt,
                        Double x, Double y) {
        this.postId = postId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.scrapCount = scrapCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.x = x;
        this.y = y;
    }


    public Long getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getScrapCount() {
        return scrapCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Double getX() { return x; }

    public Double getY() { return y; }
}
