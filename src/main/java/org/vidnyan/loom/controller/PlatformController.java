package org.vidnyan.loom.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Log4j2
@RestController
public class PlatformController {
    HashSet threadNames = new HashSet<>();
    long time;
    @GetMapping("/process")
    public void platformThread() throws InterruptedException {
        final var start = Instant.now();
       log.info("Current Thread : {} " , Thread.currentThread());
        TimeUnit.MILLISECONDS.sleep(100L);
       // threadNames.add(Thread.currentThread().toString().substring(Thread.currentThread().toString().indexOf("worker")));
        time = time + Duration.between(start, Instant.now()).toMillis();
    }

    @GetMapping("/threadCount")
    public long getNumberOfThreads() {
        threadNames.forEach(o -> log.info("Thread Name : {}", o));
        return threadNames.size();
    }

    @GetMapping("/time")
    public long getTimeInMili() {
        return time;
    }

    @GetMapping("/resetTime")
    public void resetTime() {
        time = 0L;
    }
    @GetMapping("/resetThreadCount")
    public void restThreadCount() {
        threadNames = new HashSet();
    }




}
