package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.User;
import com.on_bapsang.backend.entity.UserFavoriteDish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteDishRepository extends JpaRepository<UserFavoriteDish, Long> {
    void deleteAllByUser(User user);
}
