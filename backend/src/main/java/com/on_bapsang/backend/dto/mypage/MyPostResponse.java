package com.on_bapsang.backend.dto.mypage;

import java.util.List;

public class MyPostResponse {

    private List<MyPost> posts;

    public MyPostResponse(List<MyPost> posts) {
        this.posts = posts;
    }

    public List<MyPost> getPosts() {
        return posts;
    }
}
