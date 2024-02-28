package org.vidnyan.loom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class LoomOriTest {



    private ConcurrentMap<String, Integer> threadsCountCache;
    private int workTaskSizeMax;
    private long timeToSleepInMs;

    @BeforeEach
    void setUp() {
        threadsCountCache = new ConcurrentHashMap<>();
        workTaskSizeMax = 5000;
        timeToSleepInMs = 1000L;
    }

    @Test
    @DisplayName("Test platform thread using Thread constructor")
    void testPlatformThreadUsingThreadConstructor() throws InterruptedException {
        final var platformThread = new Thread(() -> System.out.printf("I am running inside thread=%s%n", Thread.currentThread()));
        platformThread.start();
        platformThread.join();
    }

    @Test
    @DisplayName("Test platform thread using Thread.ofPlatform() method")
    void testPlatformThreadUsingThreadOfPlatformMethod() throws InterruptedException {
        final var platformThread = Thread.ofPlatform().unstarted(() -> System.out.printf("I am running inside thread=%s%n", Thread.currentThread()));
        platformThread.start();
        platformThread.join();
    }

    @Test
    @DisplayName("Test virtual thread using Thread.ofVirtual() method")
    void testVirtualThreadUsingThreadOfVirtualMethod() throws InterruptedException {
        final var virtualThread = Thread.ofVirtual().unstarted(() -> System.out.printf("I am running inside thread=%s%n", Thread.currentThread()));
        virtualThread.start();
        virtualThread.join();
    }

    @Test
    @DisplayName("Test multiple virtual threads")
    void testMultipleVirtualThreads() throws InterruptedException {
        final Runnable runnable = () -> System.out.printf("I am running inside thread=%s%n", Thread.currentThread());
        final List<Thread> virtualThreads = new ArrayList<>();
        for (var i = 0; i < 3; i++) {
            virtualThreads.add(Thread.ofVirtual().unstarted(runnable));
        }
        for (final var virtualThread : virtualThreads) {
            virtualThread.start();
        }
        for (final var virtualThread : virtualThreads) {
            virtualThread.join();
        }
    }

    @Test
    @DisplayName("Test large number of virtual threads")
    void testLargeNumberOfVirtualThreads() {
        final var start = Instant.now();
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, workTaskSizeMax).forEach(i -> executor.submit(() -> {
                        TimeUnit.MILLISECONDS.sleep(timeToSleepInMs);
                        threadsCountCache.merge(Thread.currentThread().getName(), 1, Integer::sum);
                        return i;
                    }));
        }  // executor.close() is called implicitly, and waits
        final var timeElapsed = (Duration.between(start, Instant.now()).toMillis());
        displayStats("Virtual Threads", timeElapsed);
    }

    @Test
    @DisplayName("Test large number of cached platform threads")
    void testLargeNumberOfCachedPlatformThreads() {
        final var start = Instant.now();
        try (final var executor = Executors.newCachedThreadPool()) {
            IntStream.range(0, 1000).forEach(i -> executor.submit(
                    () ->
                    {
                        TimeUnit.MILLISECONDS.sleep(timeToSleepInMs);
                        threadsCountCache.merge(Thread.currentThread().getName(), 1, Integer::sum);
                        return i;
                    }));
        }
        final var timeElapsed = (Duration.between(start, Instant.now()).toMillis());
        displayStats("Cached Platform Threads", timeElapsed);
    }

    @Test
    @DisplayName("Test large number of fixed pool platform threads")
    @Disabled
    void testLargeNumberOfFixedPoolPlatformThreads() {
        final var start = Instant.now();
        try (final var executor = Executors.newFixedThreadPool(200)) {
            IntStream.range(0, workTaskSizeMax).forEach(i -> executor.submit(
                    () ->
                    {
                        TimeUnit.MILLISECONDS.sleep(timeToSleepInMs);
                        //TimeUnit.MILLISECONDS.sleep(1L);
                        threadsCountCache.merge(Thread.currentThread().getName(), 1, Integer::sum);
                        return i;
                    }));
        }
        final var timeElapsed = (Duration.between(start, Instant.now()).toMillis());
        displayStats("Fixed Pool Platform Threads", timeElapsed);
    }

    private void displayStats(final String threadPool, final long timeElapsed) {
        System.out.printf("[%s] Total Latency: [%d ms]%n",
                threadPool, timeElapsed);
        System.out.printf("[%s] Throughput: [%.2f req per ms]%n",
                threadPool, (double) (workTaskSizeMax / timeElapsed));
        System.out.printf("[%s] No of threads used: [%d]%n",
                threadPool, threadsCountCache.size());
        System.out.printf("[%s] threadsCountCache: [%s] %n%n",
                threadPool, threadsCountCache);
    }


}

/*



Platform Thread Total Latency: [454 ms]
Platform Thread  Throughput: [2.00 req per ms]
Platform Thread No of threads used: [526]
No of Platform Threads : [1000]




Virtual Thread Total Latency: [215 ms]
Virtual Thread  Throughput: [4.00 req per ms]
Virtual Thread No of threads used: [4]
No of Virtual Threads : [1000]


 */
