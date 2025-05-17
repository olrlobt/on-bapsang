package com.on_bapsang.backend.service;

import com.on_bapsang.backend.dto.PostRequest;
import com.on_bapsang.backend.entity.Post;
import com.on_bapsang.backend.entity.User;
import com.on_bapsang.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post create(PostRequest request, User user, String imageUrl) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setRecipeTag(request.getRecipeTag());
        post.setImageUrl(imageUrl);
        post.setUser(user);
        return postRepository.save(post);
    }
}
