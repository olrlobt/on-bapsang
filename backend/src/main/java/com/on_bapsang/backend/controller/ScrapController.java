package com.on_bapsang.backend.controller;

import com.on_bapsang.backend.dto.ApiResponse;
import com.on_bapsang.backend.security.UserDetailsImpl;
import com.on_bapsang.backend.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/scrap")
public class ScrapController {

    private final ScrapService scrapService;

    // 스크랩 토글
    @PostMapping("/{postId}")
    public ResponseEntity<?> toggleScrap(@PathVariable Long postId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean isScrapped = scrapService.toggleScrap(postId, userDetails.getUser());

        String message = isScrapped ? "스크랩 완료" : "스크랩 취소";
        return ResponseEntity.ok(ApiResponse.success(message, isScrapped));
    }
}
