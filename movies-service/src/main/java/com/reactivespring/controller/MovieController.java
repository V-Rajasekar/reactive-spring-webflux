package com.reactivespring.controller;

import java.util.List;

import com.reactivespring.client.MovieInfoRestClient;
import com.reactivespring.client.MovieReviewRestClient;
import com.reactivespring.domain.Movie;
import com.reactivespring.domain.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/v1")
public class MovieController {

    private final MovieInfoRestClient movieInfoRestClient;

    private final MovieReviewRestClient movieReviewRestClient;

    @Autowired
    public MovieController(MovieInfoRestClient movieInfoRestClient, MovieReviewRestClient movieReviewRestClient) {
        this.movieInfoRestClient = movieInfoRestClient;
        this.movieReviewRestClient = movieReviewRestClient;
    }

    @GetMapping("/movies/{movieId}")
    public Mono<Movie> getMovieDetails(@PathVariable String movieId) {
        log.info("Get Movie details for movieId:{}", movieId);
        return movieInfoRestClient.retrieveMovieInfo(movieId)
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewListMono = movieReviewRestClient.getMovieReviews(movieId).collectList();
                    return reviewListMono.map(reviews -> new Movie(movieInfo, reviews));
                });
    }
}
