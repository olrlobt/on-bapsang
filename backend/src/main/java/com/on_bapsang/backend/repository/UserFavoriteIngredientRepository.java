package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.User;
import com.on_bapsang.backend.entity.UserFavoriteIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteIngredientRepository extends JpaRepository<UserFavoriteIngredient, Long> {
    void deleteAllByUser(User user);
}
