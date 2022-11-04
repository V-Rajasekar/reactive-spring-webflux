package com.reactivespring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = MovieInfoController.class)
@AutoConfigureWebTestClient
public class MovieInfoControllerUnitTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    MovieInfoService mockMovieInfoService;

    @Test
    void getAllMovieInfo() {
        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        Flux<MovieInfo> movieInfoFlux = Flux.fromIterable(movieinfos);
        when(mockMovieInfoService.getAllMovieInfo()).thenReturn(movieInfoFlux);

        webTestClient.get()
                .uri("/v1/movieinfos")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .consumeWith(mInfo -> {
                    assertNotNull(mInfo);
                    var actual = mInfo.getResponseBody();
                    assertEquals(3, actual.size());
                });
    }

    @Test
    void getMovieInfoById() {
        MovieInfo movieInfoDarkNight
                = new MovieInfo("abc", "Dark Knight Rises",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        when(mockMovieInfoService.getMovieInfoById(anyString())).thenReturn(Mono.just(movieInfoDarkNight));

        webTestClient.get()
                .uri("/v1/movieinfos/abc")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(mInfo -> {
                    assertNotNull(mInfo);
                    var actual = mInfo.getResponseBody();
                    assertEquals("Dark Knight Rises", actual.getName());
                });
    }

    @Test
    void createMovieInfo() {
        MovieInfo data = new MovieInfo(null, "Batman Begins",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        var movieInfo = Mono.just(data);

        when(mockMovieInfoService.addMovieInfo(isA(MovieInfo.class))).thenReturn(movieInfo);

        webTestClient.post()
                .uri("/v1/movieinfos")
                .bodyValue(data)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(mInfo -> {
                    assertNotNull(mInfo);
                    var actual = mInfo.getResponseBody();
                    assertEquals("Batman Begins", actual.getName());
                });
    }
}
