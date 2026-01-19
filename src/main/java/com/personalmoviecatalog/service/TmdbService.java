package com.personalmoviecatalog.service;

import com.personalmoviecatalog.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class TmdbService {

    private final WebClient webClient;
    private final String imageBaseUrl;

    public TmdbService(
            @Value("${tmdb.api.base-url}") String baseUrl,
            @Value("${tmdb.api.key}") String apiKey,
            @Value("${tmdb.api.image-base-url}") String imageBaseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Accept", "application/json")
                .build();
        this.imageBaseUrl = imageBaseUrl;
    }

    @SuppressWarnings("unchecked")
    public List<MovieDTO> searchMovies(String query) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/movie")
                            .queryParam("query", query)
                            .queryParam("language", "es-ES")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("results")) {
                return Collections.emptyList();
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            return results.stream()
                    .limit(12)
                    .map(this::mapToMovieDTO)
                    .toList();

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private MovieDTO mapToMovieDTO(Map<String, Object> movieData) {
        MovieDTO dto = new MovieDTO();

        Object idObj = movieData.get("id");
        dto.setTmdbId(idObj != null ? ((Number) idObj).longValue() : null);
        dto.setTitle((String) movieData.get("title"));

        String releaseDate = (String) movieData.get("release_date");
        dto.setYear(releaseDate != null && releaseDate.length() >= 4 ? releaseDate.substring(0, 4) : "N/A");

        String posterPath = (String) movieData.get("poster_path");
        dto.setPosterPath(posterPath);
        dto.setFullPosterUrl(posterPath != null ? imageBaseUrl + posterPath : null);

        return dto;
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    @SuppressWarnings("unchecked")
    public List<MovieDTO> getPopularMovies() {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/popular")
                            .queryParam("language", "es-ES")
                            .queryParam("page", 1)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("results")) {
                return Collections.emptyList();
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            return results.stream()
                    .limit(10)
                    .map(this::mapToMovieDTO)
                    .toList();

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
