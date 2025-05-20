package com.on_bapsang.backend.service;

import com.on_bapsang.backend.dto.DishDto;
import com.on_bapsang.backend.dto.RecommendRequest;
import com.on_bapsang.backend.dto.RecommendResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class RecommendationService {
    private final WebClient aiWebClient;

    public RecommendationService(WebClient aiWebClient) {
        this.aiWebClient = aiWebClient;
    }

    public RecommendResponse getRecommendations(RecommendRequest request) {
        Mono<RecommendResponse> respMono = aiWebClient.post()
                .uri("/recommend")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(RecommendResponse.class);
        return respMono.block();
    }
}