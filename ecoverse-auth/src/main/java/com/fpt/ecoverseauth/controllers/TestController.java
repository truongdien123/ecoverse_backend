package com.fpt.ecoverseauth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hash-password")
    public Map<String, Object> hashPassword(@RequestParam String password) {
        String hashed = passwordEncoder.encode(password);
        
        Map<String, Object> result = new HashMap<>();
        result.put("plainPassword", password);
        result.put("hashedPassword", hashed);
        result.put("length", hashed.length());
        
        return result;
    }

    @GetMapping("/verify-password")
    public Map<String, Object> verifyPassword(
            @RequestParam String plain,
            @RequestParam String hashed) {
        
        boolean matches = passwordEncoder.matches(plain, hashed);
        
        Map<String, Object> result = new HashMap<>();
        result.put("plainPassword", plain);
        result.put("hashedPassword", hashed);
        result.put("matches", matches);
        
        return result;
    }
}
