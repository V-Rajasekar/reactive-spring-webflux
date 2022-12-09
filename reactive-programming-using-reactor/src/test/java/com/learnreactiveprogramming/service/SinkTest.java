package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Sinks;

public class SinkTest {

    @Test
    public void testSink() {

        //Creating a sink
        Sinks.Many<Integer> replaySink =  Sinks.many().replay().all();

        //producing
        replaySink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        replaySink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        //Consuming
        replaySink.asFlux().subscribe(s1 ->
                System.out.println("Subscription 1: " + s1));

        replaySink.asFlux().subscribe(s2 ->
                System.out.println("Subscription 2: " + s2));

        replaySink.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);


    }

    @Test
    public void testMulticastSink() {
        Sinks.Many<Integer> multiCast = Sinks.many().multicast().onBackpressureBuffer();
        multiCast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        multiCast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        //Consuming
        multiCast.asFlux().subscribe(s1 ->
                System.out.println("Subscription 1: " + s1));

        multiCast.asFlux().subscribe(s2 ->
                System.out.println("Subscription 2: " + s2));

        multiCast.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Test
    public void testUniCastSink() {
        Sinks.Many<Integer> uniCast = Sinks.many().unicast().onBackpressureBuffer();
        uniCast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        uniCast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        //Consuming
        uniCast.asFlux().subscribe(s1 ->
                System.out.println("Subscription 1: " + s1));

        uniCast.asFlux().subscribe(s2 ->
                System.out.println("Subscription 2: " + s2));

        uniCast.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
