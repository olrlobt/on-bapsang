package com.on_bapsang.backend.service;

import com.on_bapsang.backend.dto.LoginRequest;
import com.on_bapsang.backend.dto.TokenResponse;
import com.on_bapsang.backend.entity.User;
import com.on_bapsang.backend.jwt.JwtUtil;
import com.on_bapsang.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public TokenResponse login(LoginRequest request) {
        // 사용자 정보 조회
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    System.out.println("아이디 없음: " + request.getUsername());
                    return new IllegalArgumentException("존재하지 않는 아이디입니다.");
                });

        if (user.isDeleted()) {
            throw new IllegalArgumentException("탈퇴한 계정입니다.");
        }


        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("비밀번호 불일치");
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // Access Token & Refresh Token 발급
        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getUserId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        // Refresh Token 저장
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // 두 토큰을 JSON으로 반환
        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse reissueTokens(String refreshToken) {
        // Bearer 제거
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        String username = jwtUtil.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh Token이 일치하지 않습니다.");
        }

        // 새 토큰 발급
        String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getUserId());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        // DB에 Refresh Token 업데이트
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public void logout(User user) {
        user.setRefreshToken(null);
        userRepository.save(user);
    }


}
