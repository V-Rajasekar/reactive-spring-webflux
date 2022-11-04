package com.learnreactiveprogramming.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    public void testFluxName() {
        StepVerifier.create(fluxAndMonoGeneratorService.nameFlux())
                //        .expectNext("Alex", "Ben", "Charlie");
                .expectNextCount(3)
                .verifyComplete();

        StepVerifier.create(fluxAndMonoGeneratorService.nameFlux())
                .expectNext("Alex")
                .expectNextCount(2)
                .verifyComplete();

    }

    @Test
    void nameFlux_map() {
        StepVerifier.create(fluxAndMonoGeneratorService.nameFlux_map())
                .expectNextCount(2)
                .expectNext("ALEX", "CHARLIE")
                .verifyComplete();
    }

    @Test
    void nameFlux_flatmap_async() {
        StepVerifier.create(fluxAndMonoGeneratorService.nameFlux_flatmap_async())
                .expectNextCount(11)
                .verifyComplete();
    }

    @Test
    void nameMono_flatMap() {
        Mono<List<String>> input = fluxAndMonoGeneratorService.nameMono_flatMap("alex");
        StepVerifier.create(input).expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void nameMono_flatMapMany() {
        Flux<String> input = fluxAndMonoGeneratorService.nameMono_flatMapMany("alex");
        StepVerifier.create(input).expectNext("A","L","E","X")
                .verifyComplete();
    }

    @Test
    void nameFlux_transform() {
        StepVerifier.create(fluxAndMonoGeneratorService.nameFlux_transform())
               // .expectNextCount(2)
                .expectNext("ALEX", "CHARLIE")
                .verifyComplete();
    }

    @Test
    void nameFlux_defaultIfEmpty() {
        StepVerifier.create(fluxAndMonoGeneratorService.nameFlux_defaultIfEmpty(9))
                // .expectNextCount(2)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void nameFlux_switchIfEmpty() {
        StepVerifier.create(fluxAndMonoGeneratorService.nameFlux_switchIfEmpty(9))
                // .expectNextCount(2)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void explore_merge_flux() {
        StepVerifier.create(fluxAndMonoGeneratorService.explore_merge_flux())
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void explore_merge_mono() {
        StepVerifier.create(fluxAndMonoGeneratorService.explore_merge_mono())
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void explore_mergesequentially_flux() {
        StepVerifier.create(fluxAndMonoGeneratorService.explore_mergesequentially_flux())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_zip_flux() {
        StepVerifier.create(fluxAndMonoGeneratorService.explore_zip_flux())
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void explore_zip3_flux() {
        StepVerifier.create(fluxAndMonoGeneratorService.explore_zip3_flux())
                .expectNext("AD1", "BE2", "CF3")
                .verifyComplete();
    }

    @Test
    void explore_zipWith_mono() {
        StepVerifier.create(fluxAndMonoGeneratorService.explore_zipWith_mono())
                .expectNext("AD")
                .verifyComplete();
    }
}