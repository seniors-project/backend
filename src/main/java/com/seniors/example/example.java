package com.seniors.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class example {

    @RequestMapping("/ex")
    public String ex(){
        return "hello my name is apple......I'm sweet";
    }


    @GetMapping("/ex3")
    public void slackErrorSampleController() {
        log.info("this log is info");
        log.warn("this log is warn");
        log.error("this log is error");
    }
}
