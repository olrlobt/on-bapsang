package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.Post;
import com.on_bapsang.backend.entity.Scrap;
import com.on_bapsang.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByUserAndPost(User user, Post post);
    List<Scrap> findAllByUser(User user);
    void deleteByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
}

