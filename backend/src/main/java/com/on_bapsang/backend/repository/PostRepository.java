package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.dto.mypage.MyPost;
import com.on_bapsang.backend.dto.mypage.ScrappedPost;
import com.on_bapsang.backend.dto.PostSummary;
import com.on_bapsang.backend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시글 목록 조회 (검색 X)
    @Query("SELECT new com.on_bapsang.backend.dto.PostSummary(" +
            "p.id, p.title, p.imageUrl, p.scrapCount, p.commentCount, p.createdAt, p.user.nickname) " +
            "FROM Post p")
    Page<PostSummary> findPostSummariesWithUser(Pageable pageable);

    // 게시글 목록 조회 (검색 O)
    @Query("SELECT new com.on_bapsang.backend.dto.PostSummary(" +
            "p.id, p.title, p.imageUrl, p.scrapCount, p.commentCount, p.createdAt, p.user.nickname) " +
            "FROM Post p " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PostSummary> findPostSummariesWithUser(@Param("keyword") String keyword, Pageable pageable);

    // 마이페이지 내가 쓴 글
    @Query("SELECT new com.on_bapsang.backend.dto.mypage.MyPost(" +
            "p.id, p.title, p.content, p.imageUrl, p.scrapCount, p.commentCount, p.createdAt) " +
            "FROM Post p WHERE p.user.userId = :userId")
    Page<MyPost> findMyPostsByUser(@Param("userId") Long userId, Pageable pageable);

    // 마이페이지 스크랩한 글
    @Query("SELECT new com.on_bapsang.backend.dto.mypage.ScrappedPost(" +
            "p.id, p.title, p.imageUrl, p.scrapCount, p.commentCount, p.createdAt) " +
            "FROM Scrap s JOIN s.post p " +
            "WHERE s.user.userId = :userId")
    Page<ScrappedPost> findScrappedPostsByUser(@Param("userId") Long userId, Pageable pageable);
}
