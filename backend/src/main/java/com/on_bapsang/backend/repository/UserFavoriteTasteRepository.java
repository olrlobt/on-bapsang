package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.User;
import com.on_bapsang.backend.entity.UserFavoriteTaste;
import com.on_bapsang.backend.entity.Taste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

public interface UserFavoriteTasteRepository extends JpaRepository<UserFavoriteTaste, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM UserFavoriteTaste uft WHERE uft.user = :user")
    void deleteAllByUser(@Param("user") User user);

    // 중복 저장 방지용
    boolean existsByUserAndTaste(User user, Taste taste);
}
