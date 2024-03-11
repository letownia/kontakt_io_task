package org.temperature.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemperatureController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Temperature Collector potential future project manager from Kontakt IO!";
    }



}
