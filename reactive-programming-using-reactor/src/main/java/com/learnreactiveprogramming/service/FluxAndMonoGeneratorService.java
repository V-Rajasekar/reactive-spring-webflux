package com.learnreactiveprogramming.service;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoGeneratorService {

    public Flux<String> nameFlux() {
        return Flux.fromIterable(List.of("Alex", "Ben", "Charlie")).log();
    }

    public Flux<String> nameFlux_map() {
        return Flux.fromIterable(List.of("Alex", "Ben", "Charlie"))
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase).log();
    }


    public Flux<String> nameFlux_transform() {
        //Creating a function functional interface
        Function<Flux<String>, Flux<String>> fluxFunction = name -> name.filter(s -> s.length() > 3)
                .map(String::toUpperCase);

        return Flux.fromIterable(List.of("Alex", "Ben", "Charlie"))
                .transform(fluxFunction)
               .log();
    }

    public Flux<String> nameFlux_defaultIfEmpty(int length) {
        //Creating a function functional interface
        Function<Flux<String>, Flux<String>> fluxFunction = name -> name.filter(s -> s.length() > length)
                .map(String::toUpperCase);

        return Flux.fromIterable(List.of("Alex", "Ben", "Charlie"))
                .transform(fluxFunction)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> nameFlux_switchIfEmpty(int length) {
        //Creating a function functional interface
        Function<Flux<String>, Flux<String>> fluxFunction = name -> name.filter(s -> s.length() > length)
                .map(String::toUpperCase);

        Flux<String> defaultFlux = Flux.just("default").transform(fluxFunction);

        return Flux.fromIterable(List.of("Alex", "Ben", "Charlie"))
                .transform(fluxFunction)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> explore_concat() {
        Mono<String> a = Mono.just("A");
        Mono<String> b = Mono.just("B");
        Flux<String> stringFlux = a.concatWith(b).log();

        Flux<String> fluxa = Flux.just("A", "B", "C");
        Flux<String> fluxb = Flux.just("D", "E", "F");

        Flux<String> fluxConcat = Flux.concat(fluxa, fluxb);
        return fluxConcat;
    }

    public Flux<String> explore_merge_flux() {
        Flux<String> fluxa = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(90));
        Flux<String> fluxb = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(100));
        return Flux.merge(fluxa, fluxb);
    }

    public Flux<String> explore_merge_mono() {
        Mono<String> a = Mono.just("A").delaySubscription(Duration.ofMillis(90));
        Mono<String> b = Mono.just("B").delaySubscription(Duration.ofMillis(100));
        return a.mergeWith(b);
    }

    public Flux<String> explore_mergesequentially_flux() {
        Flux<String> fluxa = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(90));
        Flux<String> fluxb = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(100));
        return Flux.mergeSequential(fluxa, fluxb);
    }

    public Flux<String> explore_zip_flux() {
        Flux<String> fluxa = Flux.just("A", "B", "C");
        Flux<String> fluxb = Flux.just("D", "E", "F");
        return Flux.zip(fluxa, fluxb, (first, second) -> first + second); // AD, BE, CF
    }

    public Flux<String> explore_zip3_flux() {
        Flux<String> fluxa = Flux.just("A", "B", "C");
        Flux<String> fluxb = Flux.just("D", "E", "F");
        Flux<String> fluxn = Flux.just("1", "2", "3");
        return Flux.zip(fluxa, fluxb, fluxn).map(t4 -> t4.getT1() + t4.getT2() + t4.getT3()); // AD1, BE2, CF3
    }

    public Mono<String> explore_zipWith_mono() {
        Mono<String> monoa = Mono.just("A");
        Mono<String> monob = Mono.just("D");
        return monoa.zipWith(monob).map(t1-> t1.getT1() + t1.getT2()); // AD, BE, CF
    }

    public Flux<String> nameFlux_flatmap_async() {
        return Flux.fromIterable(List.of("Alex", "Ben", "Charlie"))
                .filter(s -> s.length() > 3)
                .flatMap(this::splitStringWithDelay)
                .map(String::toUpperCase).log();
    }

    public Flux<String> nameFlux_flatmap() {
        return Flux.fromIterable(List.of("Alex", "Ben", "Charlie"))
                .filter(s -> s.length() > 3)
                .flatMap(this::splitString)
                .map(String::toUpperCase).log();
    }

    //ALEX -> Flux(A,L,E,X)
    public Flux<String> splitString(String name) {
        return Flux.fromArray(name.split(""));
    }

    public Flux<String> splitStringWithDelay(String name) {
        int delay = new Random()
                .nextInt(1000);
        return Flux.fromArray(name.split("")).delayElements(Duration.ofMillis(delay));
    }

    public Mono<String> nameMono() {
        return Mono.just("Alex").log();
    }

    public Mono<List<String>> nameMono_flatMap(String name) {
        return Mono.just(name)
                .map(String::toUpperCase)
                .flatMap(this::splitStringMono).log(); //Mono <List of A, L, E, X>
    }

    private Mono<List<String>> splitStringMono(String value) {
        var charArray = value.split("");
        return Mono.just(List.of(charArray));  //Mono <List of A, L, E, X>
    }

    public Flux<String> nameMono_flatMapMany(String name) {
        return Mono.just(name)
                .map(String::toUpperCase)
                .flatMapMany(this::splitStringWithDelay).log(); //Mono <List of A, L, E, X>
    }

    public static void main(String args[]) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.nameFlux().subscribe(name -> {
            System.out.println("Name is:" + name);
        });

        fluxAndMonoGeneratorService.nameFlux().subscribe(name -> System.out.println("Name is:" + name));
    }
}
