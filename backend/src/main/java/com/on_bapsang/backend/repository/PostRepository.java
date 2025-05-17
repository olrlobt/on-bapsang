package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시글 제목으로 키워드 검색 (대소문자 구분 없이 포함 여부 검사)
    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
