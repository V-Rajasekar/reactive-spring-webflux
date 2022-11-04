package com.reactivespring.client;

import com.reactivespring.domain.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class MovieReviewRestClient {

    private WebClient webClient;

    @Autowired
    public MovieReviewRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Value("${restclient.review}")
    private String movieReviewUrl;

    public Flux<Review> getMovieReviews(String movieId) {
        var url = UriComponentsBuilder.fromHttpUrl(movieReviewUrl)
                .queryParam("movieInfoId", movieId)
                .buildAndExpand().toUriString();

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Review.class);
    }
}
