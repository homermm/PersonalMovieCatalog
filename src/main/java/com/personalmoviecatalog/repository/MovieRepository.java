package com.personalmoviecatalog.repository;

import com.personalmoviecatalog.model.Movie;
import com.personalmoviecatalog.model.MovieStatus;
import com.personalmoviecatalog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByUserOrderByIdDesc(User user);

    List<Movie> findByUserAndStatusOrderByIdDesc(User user, MovieStatus status);

    Optional<Movie> findByUserAndTmdbId(User user, Long tmdbId);

    boolean existsByUserAndTmdbId(User user, Long tmdbId);
}
