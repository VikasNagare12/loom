package org.vidnyan.loom;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class LoomApplication {
	public static void main(String[] args) {
		SpringApplication.run(LoomApplication.class, args);
		log.info("Loom project started");
	}
}