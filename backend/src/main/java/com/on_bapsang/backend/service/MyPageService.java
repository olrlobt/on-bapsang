package com.on_bapsang.backend.service;

import com.on_bapsang.backend.dto.mypage.MyPost;
import com.on_bapsang.backend.dto.mypage.ScrappedPost;
import com.on_bapsang.backend.entity.User;
import com.on_bapsang.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;

    public Page<MyPost> getMyPosts(User user, Pageable pageable) {
        return postRepository.findMyPostsByUser(user.getUserId(), pageable);
    }

    public Page<ScrappedPost> getScrappedPosts(User user, Pageable pageable) {
        return postRepository.findScrappedPostsByUser(user.getUserId(), pageable);
    }


}
