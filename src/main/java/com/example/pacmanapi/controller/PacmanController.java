package com.example.pacmanapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PacmanController {

    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok().body("Hello");
    }

}
