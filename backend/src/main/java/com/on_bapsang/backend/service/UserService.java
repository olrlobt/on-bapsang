package com.on_bapsang.backend.service;

import com.on_bapsang.backend.dto.LoginRequest;
import com.on_bapsang.backend.dto.UpdateUserRequest;
import com.on_bapsang.backend.exception.CustomException;
import org.springframework.http.HttpStatus;
import com.on_bapsang.backend.dto.SignupRequest;
import com.on_bapsang.backend.entity.*;
import com.on_bapsang.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TasteRepository tasteRepository;
    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;
    private final UserFavoriteTasteRepository userFavoriteTasteRepository;
    private final UserFavoriteDishRepository userFavoriteDishRepository;
    private final UserFavoriteIngredientRepository userFavoriteIngredientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(SignupRequest request) {
        // 0. 아이디 중복 검사
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT);
        }

        // 1. 재료는 필수
        if (request.getFavoriteIngredientIds() == null || request.getFavoriteIngredientIds().isEmpty()) {
            throw new CustomException("좋아하는 음식(재료)은 최소 1개 이상 선택해야 합니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        // 3. User 저장
        User user = User.builder()
                .username(request.getUsername())
                .password(encryptedPassword)
                .nickname(request.getNickname())
                .country(request.getCountry())
                .age(request.getAge())
                .location(request.getLocation())
                .build();
        userRepository.save(user);

        // 4. 연관 Taste 저장 (선택)
        if (request.getFavoriteTasteIds() != null) {
            List<Taste> tastes = tasteRepository.findAllById(request.getFavoriteTasteIds());
            for (Taste taste : tastes) {
                userFavoriteTasteRepository.save(new UserFavoriteTaste(null, user, taste));
            }
        }

        // 5. 연관 Dish 저장 (선택)
        if (request.getFavoriteDishIds() != null) {
            List<Dish> dishes = dishRepository.findAllById(request.getFavoriteDishIds());
            for (Dish dish : dishes) {
                userFavoriteDishRepository.save(new UserFavoriteDish(null, user, dish));
            }
        }

        // 6. 연관 Ingredient 저장 (필수)
        List<Ingredient> ingredients = ingredientRepository.findAllById(request.getFavoriteIngredientIds());
        for (Ingredient ingredient : ingredients) {
            userFavoriteIngredientRepository.save(new UserFavoriteIngredient(null, user, ingredient, "좋아함"));
        }
    }

    public void withdraw(User user) {
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserInfo(User user, UpdateUserRequest request) {
        if (request.getNickname() != null) user.setNickname(request.getNickname());
        if (request.getAge() != null) user.setAge(request.getAge());
        userRepository.save(user); // 사용자 기본 정보 먼저 저장

        // Taste 수정
        if (request.getFavoriteTasteIds() != null) {
            userFavoriteTasteRepository.deleteAllByUser(user); // 기존 삭제
            List<Taste> tastes = tasteRepository.findAllById(request.getFavoriteTasteIds());
            for (Taste taste : tastes) {
                userFavoriteTasteRepository.save(new UserFavoriteTaste(null, user, taste));
            }
        }

        // Dish 수정
        if (request.getFavoriteDishIds() != null) {
            userFavoriteDishRepository.deleteAllByUser(user);
            List<Dish> dishes = dishRepository.findAllById(request.getFavoriteDishIds());
            for (Dish dish : dishes) {
                userFavoriteDishRepository.save(new UserFavoriteDish(null, user, dish));
            }
        }

        // Ingredient 수정
        if (request.getFavoriteIngredientIds() != null) {
            userFavoriteIngredientRepository.deleteAllByUser(user);
            List<Ingredient> ingredients = ingredientRepository.findAllById(request.getFavoriteIngredientIds());
            for (Ingredient ingredient : ingredients) {
                userFavoriteIngredientRepository.save(new UserFavoriteIngredient(null, user, ingredient, "좋아함"));
            }
        }
    }





}
