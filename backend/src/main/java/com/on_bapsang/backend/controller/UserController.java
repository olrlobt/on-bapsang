package com.on_bapsang.backend.controller;

import com.on_bapsang.backend.dto.*;
import com.on_bapsang.backend.entity.User;
import com.on_bapsang.backend.exception.CustomException;
import com.on_bapsang.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> registerUser(@RequestBody SignupRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new CustomException("인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED);
        }

        UserInfoResponse response = new UserInfoResponse(user);
        return ResponseEntity.ok(ApiResponse.success("내 정보 조회 성공", response));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(@AuthenticationPrincipal User user) {
        userService.withdraw(user);
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴가 완료되었습니다."));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateMyInfo(@AuthenticationPrincipal User user,
                                                          @RequestBody UpdateUserRequest request) {
        userService.updateUserInfo(user, request);
        return ResponseEntity.ok(ApiResponse.success("회원정보가 수정되었습니다."));
    }



}
