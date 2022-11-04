package com.reactivespring.repository;
import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

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
    void findAll() {
        //given

        //when
        var moviesInfoFlux = movieInfoRepository.findAll().log();

        //then
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void movieInfo_CRUD() {
        //Create
        MovieInfo ps1 = new MovieInfo(null, "Ponniyin Selvan",
                2021, List.of("Vikram", "Aishwarya", "Karthik"), LocalDate.parse("2021-10-21"));
        var monoMovieInfo= movieInfoRepository.save(ps1);

        //Retrieve

        Mono<MovieInfo> ponniyin_selvan = movieInfoRepository.findById("Ponniyin Selvan");
        StepVerifier.create(ponniyin_selvan)
                .assertNext(movieInfo -> assertEquals("Ponniyin Selvan", movieInfo.getName()));

        //Update.
        MovieInfo ps1update = ponniyin_selvan.block();
        assert ps1update != null;
        ps1update.setRelease_date(LocalDate.parse("2021-09-25"));
        var updatedMonoPs1 = movieInfoRepository.save(ps1update);
        StepVerifier.create(updatedMonoPs1)
                .assertNext(movieInfo -> assertEquals("2021-09-25", movieInfo.getRelease_date()));

        //Delete
        movieInfoRepository.deleteById("Ponniyin Selvan").log().block();
        var deletePs1 = movieInfoRepository.findById("Ponniyin Selvan");
        StepVerifier.create(deletePs1).expectNextCount(0).verifyComplete();
    }
}