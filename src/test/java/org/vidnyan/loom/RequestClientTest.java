package org.vidnyan.loom;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.stream.IntStream;

@Log4j2
public class RequestClientTest {

    @Test
    @DisplayName("Platform thread request  client")
    void platformThreadRequestClient() throws InterruptedException {
        var threads = IntStream.range(0, 500)
                .mapToObj(value -> Thread.startVirtualThread(() -> {
                    RestClient.create().get()
                            .uri("http://localhost:8080/loom/platform/"+value).retrieve().body(String.class);
                })).toList();
        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Test
    @DisplayName("Platform thread request  client")
    void platformThreadRequestClient1() throws InterruptedException {
        final Runnable runnable = () -> RestClient.create().get()
                .uri("http://localhost:8080/loom/platform")
                .retrieve()
                .body(String.class);

        var threads = IntStream.range(0, 500)
                .mapToObj(value -> Thread.startVirtualThread(runnable)).toList();
        for (Thread thread : threads) {
            thread.join();
        }
    }


}
