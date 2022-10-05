package com.example.studentsinfosystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TX
 * @date 2022/10/5 19:49
 */

@RestController

public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
