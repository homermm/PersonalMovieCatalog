package com.personalmoviecatalog.service;

import com.personalmoviecatalog.dto.MovieDTO;
import com.personalmoviecatalog.model.Movie;
import com.personalmoviecatalog.model.MovieStatus;
import com.personalmoviecatalog.model.User;
import com.personalmoviecatalog.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final String imageBaseUrl;

    public MovieService(
            MovieRepository movieRepository,
            @Value("${tmdb.api.image-base-url}") String imageBaseUrl) {
        this.movieRepository = movieRepository;
        this.imageBaseUrl = imageBaseUrl;
    }

    public List<MovieDTO> getUserMovies(User user, MovieStatus status) {
        List<Movie> movies;
        if (status != null) {
            movies = movieRepository.findByUserAndStatusOrderByIdDesc(user, status);
        } else {
            movies = movieRepository.findByUserOrderByIdDesc(user);
        }

        return movies.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public MovieDTO addMovie(User user, MovieDTO dto, MovieStatus status) {
        if (movieRepository.existsByUserAndTmdbId(user, dto.getTmdbId())) {
            throw new IllegalArgumentException("Esta pelicula ya esta en tu lista");
        }

        Movie movie = new Movie();
        movie.setTmdbId(dto.getTmdbId());
        movie.setTitle(dto.getTitle());
        movie.setYear(dto.getYear());
        movie.setPosterPath(dto.getPosterPath());
        movie.setStatus(status);
        movie.setUser(user);

        Movie saved = movieRepository.save(movie);
        return convertToDTO(saved);
    }

    @Transactional
    public MovieDTO updateMovieReview(User user, Long movieId, Integer rating, String review) {
        Movie movie = movieRepository.findById(movieId)
                .filter(m -> m.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Pelicula no encontrada"));

        if (rating != null) {
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("El rating debe estar entre 1 y 5");
            }
            movie.setRating(rating);
        }
        movie.setReview(review);
        movie.setStatus(MovieStatus.WATCHED);

        return convertToDTO(movieRepository.save(movie));
    }

    @Transactional
    public MovieDTO updateMovieStatus(User user, Long movieId, MovieStatus status) {
        Movie movie = movieRepository.findById(movieId)
                .filter(m -> m.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Pelicula no encontrada"));

        movie.setStatus(status);
        return convertToDTO(movieRepository.save(movie));
    }

    @Transactional
    public void deleteMovie(User user, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .filter(m -> m.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Pelicula no encontrada"));

        movieRepository.delete(movie);
    }

    private MovieDTO convertToDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTmdbId(movie.getTmdbId());
        dto.setTitle(movie.getTitle());
        dto.setYear(movie.getYear());
        dto.setPosterPath(movie.getPosterPath());
        dto.setFullPosterUrl(movie.getPosterPath() != null ? imageBaseUrl + movie.getPosterPath() : null);
        dto.setStatus(movie.getStatus());
        dto.setRating(movie.getRating());
        dto.setReview(movie.getReview());
        return dto;
    }
}
