package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.Comment;
import com.on_bapsang.backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.children cc " +
            "LEFT JOIN FETCH cc.user " +
            "WHERE c.post = :post AND c.parent IS NULL")
    List<Comment> findByPostAndParentIsNullWithUserAndReplies(@Param("post") Post post);


}
