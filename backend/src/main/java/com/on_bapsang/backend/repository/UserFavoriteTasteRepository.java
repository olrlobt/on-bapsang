package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.User;
import com.on_bapsang.backend.entity.UserFavoriteTaste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteTasteRepository extends JpaRepository<UserFavoriteTaste, Long> {
    void deleteAllByUser(User user);
}
