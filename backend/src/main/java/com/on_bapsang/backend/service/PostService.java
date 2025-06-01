package com.on_bapsang.backend.service;

import com.on_bapsang.backend.dto.PostDetail;
import com.on_bapsang.backend.dto.PostDetailWithScrap;
import com.on_bapsang.backend.dto.PostRequest;
import com.on_bapsang.backend.dto.PostSummary;
import com.on_bapsang.backend.entity.Post;
import com.on_bapsang.backend.entity.Recipe;
import com.on_bapsang.backend.entity.User;
import com.on_bapsang.backend.repository.PostRepository;
import com.on_bapsang.backend.repository.RecipeRepository;
import com.on_bapsang.backend.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final RecipeRepository recipeRepository;
    private final ScrapService scrapService;
    private final SearchKeywordService searchKeywordService;

    // 게시글 생성
    public Post create(PostRequest request, User user, String imageUrl) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setRecipeTag(request.getRecipeTag());
        post.setImageUrl(imageUrl);
        post.setUser(user);
        return postRepository.save(post);
    }

    // 게시글 생성시 레시피 db 조회
    public List<String> getRecipeTagSuggestions(String keyword) {
        return recipeRepository.findTop10ByNameStartingWithIgnoreCase(keyword)
                .stream()
                .map(Recipe::getName)
                .toList();
    }


    // 게시글 검색
    public Page<PostSummary> getPosts(String keyword, Pageable pageable, User user) {
        if (keyword != null && !keyword.isBlank()) {
            searchKeywordService.saveRecentKeyword(user.getUserId(), keyword);
            searchKeywordService.increaseKeywordScore(keyword);
            return postRepository.findPostSummariesWithUser(keyword, pageable);
        } else {
            return postRepository.findPostSummariesWithUser(pageable);
        }
    }




    // 개별 글 검색
    @Transactional(readOnly = true)
    public PostDetail getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return new PostDetail(post);
    }

    // 로그인 유저 개별 글 검색
    @Transactional(readOnly = true)
    public PostDetailWithScrap getPostById(Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        boolean isScrapped = scrapService.isScrapped(post, user);

        return new PostDetailWithScrap(post, isScrapped);
    }




    // 게시글 수정
    public Post update(Long id, PostRequest request, User user, String imageUrl) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 작성자 검증
        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setRecipeTag(request.getRecipeTag());

        // 이미지가 새로 들어왔을 때만 업데이트
        if (imageUrl != null) {
            post.setImageUrl(imageUrl);
        }

        return postRepository.save(post);
    }

    // 게시글 삭제
    public void delete(Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 작성자 검증
        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }




}
