package com.seniors.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class example {

    @RequestMapping("/ex")
    public String ex(){
        return "hello my name is apple!!!!!!";
    }


}
