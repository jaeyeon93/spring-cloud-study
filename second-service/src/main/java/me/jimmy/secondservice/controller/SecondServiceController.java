package me.jimmy.secondservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/second-service")
public class SecondServiceController {
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Second service.";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("second-request") String header) {
        System.out.println(header);
        return "Second Request Header is " + header;
    }

    @GetMapping("/check")
    public String check() {
        return "Hi, This is message of Second Service";
    }
}
