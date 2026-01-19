package com.personalmoviecatalog.controller;

import com.personalmoviecatalog.dto.MovieDTO;
import com.personalmoviecatalog.model.MovieStatus;
import com.personalmoviecatalog.model.User;
import com.personalmoviecatalog.service.MovieService;
import com.personalmoviecatalog.service.TmdbService;
import com.personalmoviecatalog.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class MovieController {

    private final MovieService movieService;
    private final TmdbService tmdbService;
    private final UserService userService;

    public MovieController(MovieService movieService, TmdbService tmdbService, UserService userService) {
        this.movieService = movieService;
        this.tmdbService = tmdbService;
        this.userService = userService;
    }

    @GetMapping
    public String dashboard(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String search,
            Model model,
            Authentication auth) {

        User user = userService.findByUsername(auth.getName());
        model.addAttribute("userName", user.getName());

        MovieStatus statusFilter = null;
        if ("pending".equalsIgnoreCase(filter)) {
            statusFilter = MovieStatus.PENDING;
        } else if ("watched".equalsIgnoreCase(filter)) {
            statusFilter = MovieStatus.WATCHED;
        }
        List<MovieDTO> userMovies = movieService.getUserMovies(user, statusFilter);
        model.addAttribute("movies", userMovies);
        model.addAttribute("currentFilter", filter);

        if (search != null && !search.isBlank()) {
            List<MovieDTO> searchResults = tmdbService.searchMovies(search);
            model.addAttribute("searchResults", searchResults);
            model.addAttribute("searchQuery", search);
        } else {
            List<MovieDTO> popularMovies = tmdbService.getPopularMovies();
            model.addAttribute("popularMovies", popularMovies);
        }

        return "dashboard";
    }

    @PostMapping("/add")
    public String addMovie(
            @RequestParam Long tmdbId,
            @RequestParam String title,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String posterPath,
            @RequestParam String status,
            Authentication auth,
            RedirectAttributes redirect) {

        User user = userService.findByUsername(auth.getName());
        MovieDTO dto = new MovieDTO();
        dto.setTmdbId(tmdbId);
        dto.setTitle(title);
        dto.setYear(year);
        dto.setPosterPath(posterPath);

        MovieStatus movieStatus = "WATCHED".equalsIgnoreCase(status) ? MovieStatus.WATCHED : MovieStatus.PENDING;

        try {
            movieService.addMovie(user, dto, movieStatus);
            redirect.addFlashAttribute("success", "Pelicula agregada");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/update/{id}")
    public String updateMovie(
            @PathVariable Long id,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String review,
            @RequestParam(required = false) String status,
            Authentication auth,
            RedirectAttributes redirect) {

        User user = userService.findByUsername(auth.getName());

        try {
            if (status != null) {
                MovieStatus newStatus = "WATCHED".equalsIgnoreCase(status) ? MovieStatus.WATCHED : MovieStatus.PENDING;
                movieService.updateMovieStatus(user, id, newStatus);
            }
            if (rating != null || review != null) {
                movieService.updateMovieReview(user, id, rating, review);
            }
            redirect.addFlashAttribute("success", "Pelicula actualizada");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteMovie(
            @PathVariable Long id,
            Authentication auth,
            RedirectAttributes redirect) {

        User user = userService.findByUsername(auth.getName());

        try {
            movieService.deleteMovie(user, id);
            redirect.addFlashAttribute("success", "Pelicula eliminada");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard";
    }
}
