package com.personalmoviecatalog.dto;

import com.personalmoviecatalog.model.MovieStatus;

public class MovieDTO {

    private Long id;
    private Long tmdbId;
    private String title;
    private String year;
    private String posterPath;
    private String fullPosterUrl;
    private MovieStatus status;
    private Integer rating;
    private String review;

    public MovieDTO() {
    }

    public MovieDTO(Long tmdbId, String title, String year, String posterPath) {
        this.tmdbId = tmdbId;
        this.title = title;
        this.year = year;
        this.posterPath = posterPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Long tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getFullPosterUrl() {
        return fullPosterUrl;
    }

    public void setFullPosterUrl(String fullPosterUrl) {
        this.fullPosterUrl = fullPosterUrl;
    }

    public MovieStatus getStatus() {
        return status;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
