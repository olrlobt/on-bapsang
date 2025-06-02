package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);  // 회원가입 시 중복 체크용
    Optional<User> findByUsername(String username); // 로그인 시 사용자 조회용

    @Query("""
    SELECT u FROM User u
    LEFT JOIN FETCH u.favoriteDishes fd
    LEFT JOIN FETCH fd.dish
    LEFT JOIN FETCH u.favoriteIngredients fi
    LEFT JOIN FETCH fi.ingredient
    LEFT JOIN FETCH u.favoriteTastes ft
    LEFT JOIN FETCH ft.taste
    WHERE u.username = :username
""")
    Optional<User> findWithAllFavoritesByUsername(@Param("username") String username);





}

