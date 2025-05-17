package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);  // 회원가입 시 중복 체크용
    Optional<User> findByUsername(String username); // 로그인 시 사용자 조회용
}

