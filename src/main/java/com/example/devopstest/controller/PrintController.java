package com.example.devopstest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class PrintController {
    @GetMapping("/say")
    public ResponseEntity<String> saySomething(@RequestParam String words){
        return ResponseEntity.ok(words);
    }
}
