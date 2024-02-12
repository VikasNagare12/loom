package org.vidnyan.loom;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ThroughputTest {


    @Test
    @DisplayName("Test multiple virtual threads")
    void testMultipleVirtualThreads() throws InterruptedException {
        final Runnable runnable = () -> System.out.printf("I am running inside thread=%s%n", Thread.currentThread());
        var virtualThreads = IntStream.range(0, 10)
                .mapToObj(value -> Thread.ofVirtual().start(runnable))
                .toList();
        for (final var virtualThread : virtualThreads) {
            virtualThread.join();
        }
    }

    @Test
    @DisplayName("Test large number of virtual threads Throughput")
    void testLargeNumberOfVirtualThreads() {
        final var threadNames = new HashSet<>();
        final var start = Instant.now();
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 1000).forEach(i -> executor.submit(() -> {
                TimeUnit.MILLISECONDS.sleep(100L);
                threadNames.add(Thread.currentThread().toString().substring(Thread.currentThread().toString().indexOf("worker")));
                return i;
            }));
        }
        final var timeElapsed = (Duration.between(start, Instant.now()).toMillis());
        System.out.printf("Virtual Thread Total Latency: [%d ms]%n", timeElapsed);
        System.out.printf("Virtual Thread  Throughput: [%.2f req per ms]%n", (double) (1000 / timeElapsed));
        System.out.printf("Virtual Thread No of threads used: [%d]%n", threadNames.size());
        System.out.printf("No of Virtual Threads : [%s] %n%n", 1000);
    }

    @Test
    @DisplayName("Test large number of cached platform threads Throughput")
    void testLargeNumberOfCachedPlatformThreads() {
        final var threadNames = new HashSet<>();
        final var start = Instant.now();
        try (final var executor = Executors.newCachedThreadPool()) {
            IntStream.range(0, 1000).forEach(i -> executor.submit(() -> {
                        TimeUnit.MILLISECONDS.sleep(100L);
                        threadNames.add(Thread.currentThread());
                        return i;
                    }));
        }
        final var timeElapsed = (Duration.between(start, Instant.now()).toMillis());
        System.out.printf("Platform Thread Total Latency: [%d ms]%n", timeElapsed);
        System.out.printf("Platform Thread  Throughput: [%.2f req per ms]%n", (double) (1000 / timeElapsed));
        System.out.printf("Platform Thread No of threads used: [%d]%n", threadNames.size());
        System.out.printf("No of Platform Threads : [%s] %n%n", 1000);
    }

    @Test
    @DisplayName("Test large number of fixed pool platform threads Throughput")
    @Disabled
    void testLargeNumberOfFixedPoolPlatformThreads() {
        final var threadNames = new HashSet<>();
        final var start = Instant.now();
        try (final var executor = Executors.newFixedThreadPool(150)) {
            IntStream.range(0, 1000).forEach(i -> executor.submit(
                    () ->
                    {
                        TimeUnit.MILLISECONDS.sleep(100L);
                        //TimeUnit.MILLISECONDS.sleep(1L);
                        threadNames.add(Thread.currentThread());
                        return i;
                    }));
        }
        final var timeElapsed = (Duration.between(start, Instant.now()).toMillis());
        System.out.printf("Platform Thread Total Latency: [%d ms]%n", timeElapsed);
        System.out.printf("Platform Thread  Throughput: [%.10f req per ms]%n", (double) (1000 / timeElapsed));
        System.out.printf("Platform Thread No of threads used: [%d]%n", threadNames.size());
        System.out.printf("No of Platform Threads : [%s] %n%n", 200);
    }

}
