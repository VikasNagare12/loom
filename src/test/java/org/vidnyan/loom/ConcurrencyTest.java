package org.vidnyan.loom;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

@Log4j2
public class ConcurrencyTest {

    @Test
    @DisplayName("Test ::::Loom Game for java 21 virtual thread concurrency::::::")
    void testLargeNumberOfCachedPlatformThreads() throws InterruptedException {
        Thread.startVirtualThread(() ->

                IntStream.range(1, 10).forEach(value -> {
                    try {
                        log.info("Current Thread  :    {}", Thread.currentThread());
                        Thread.sleep(200);
                    } catch (InterruptedException ignored) {
                    }


                })).join();
    }
}
