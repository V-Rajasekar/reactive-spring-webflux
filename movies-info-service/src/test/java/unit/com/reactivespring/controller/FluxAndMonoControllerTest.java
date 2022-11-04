package com.reactivespring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import com.reactivespring.controller.FluxAndMonoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
public class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void getFluxInteger() {

        webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(3);
    }

    @Test
    void testMono() {

        var actual = webTestClient.get()
                .uri("/mono")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Integer.class) //Return is a List<Integer>
                .consumeWith(listExchangeResult -> {
                    var responseBody = listExchangeResult.getResponseBody();
                    assertEquals(responseBody, 1); //Applicable when used
                    // expectBodyList(Integer.class)
                });
    }

    @Test
    void getFlux_Approach2() {

        var actual = webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(actual)
                .expectNext(1, 2, 3)
                .verifyComplete();
    }

    @Test
    void getFlux_Approach3() {

        var actual = webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class) //Return is a List<Integer>
                .consumeWith(listExchangeResult -> {
                    var responseBody = listExchangeResult.getResponseBody();
                    assert Objects.requireNonNull(responseBody).size() == 3; //Applicable when used expectBodyList
                    // (Integer.class)
                });
    }

    @Test
    void testStream() {
        var actual = webTestClient.get()
                .uri("/stream")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(actual)
                .expectNext(0L, 1L, 2L, 3L)
                .thenCancel()
                .verify();
    }
}
