package com.reactivespring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MovieInfoControllerITTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieinfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void createMovieInfo() {
        MovieInfo ps1 = new MovieInfo(null, "Ponniyin Selvan",
                2021, List.of("Vikram", "Aishwarya", "Karthik"), LocalDate.parse("2021-10-21"));
        webTestClient.post()
                .uri("/movieinfos")
                .bodyValue(ps1)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(result -> {
                    MovieInfo savedMoviedInfo = result.getResponseBody();
                    assertNotNull(savedMoviedInfo);
                    assertEquals("Ponniyin Selvan", savedMoviedInfo.getMovieInfoId());
                });
    }

    @Test
    void updateMovieInfoNotFound() {
        MovieInfo ps1 = new MovieInfo(null, "Ponniyin Selvan",
                2021, List.of("Vikram", "Aishwarya", "Karthik"), LocalDate.parse("2021-10-21"));
        webTestClient.put()
                .uri("/movieinfos/{id}", "abc")
                .bodyValue(ps1)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void getAllMovieInfo() {
        webTestClient.get()
                .uri("/movieinfos")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(4);
    }

    @Test
    void getMovieInfoById() {
        webTestClient.get()
                .uri("/movieinfos/{id}", "abc")
                .exchange()
                .expectStatus().is2xxSuccessful()
              /*  .expectBody(MovieInfo.class)
                .consumeWith(retMI -> {
                    MovieInfo responseBody = retMI.getResponseBody();
                    assertEquals("Dark Knight Rises", responseBody.getMovieInfoId());
                });*/
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");
    }
}