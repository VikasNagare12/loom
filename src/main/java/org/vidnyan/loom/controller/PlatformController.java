package org.vidnyan.loom.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
@Log4j2
@RestController
public class PlatformController {

    @GetMapping("/platform/{value}")
    public String platformThread(@PathVariable String value) throws InterruptedException {

       log.info("Current Thread : {},  Request Number: {} " , Thread.currentThread(), value);
       Thread.sleep(100000);
        return "";
    }
}
