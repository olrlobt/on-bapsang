package com.on_bapsang.backend.controller;

import com.on_bapsang.backend.dto.ApiResponse;
import com.on_bapsang.backend.dto.LoginRequest;
import com.on_bapsang.backend.dto.TokenResponse;
import com.on_bapsang.backend.entity.User;
import com.on_bapsang.backend.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse tokens = loginService.login(request);
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", tokens));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        TokenResponse newTokens = loginService.reissueTokens(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("토큰 재발급 성공", newTokens));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal User user) {
        loginService.logout(user);
        return ResponseEntity.ok(ApiResponse.success("로그아웃 성공"));
    }
}
