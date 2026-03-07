package com.example.auth.controller;

import com.example.auth.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final EmailService emailService;

    public AlertController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/fire")
    public ResponseEntity<String> triggerFireAlert(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double percentage) {
        if (email != null && !email.isEmpty()) {
            emailService.sendFireAlert(email, latitude, longitude, percentage);
            return ResponseEntity.ok("Fire alert sent to " + email);
        } else {
            emailService.broadcastFireAlert(latitude, longitude, percentage);
            return ResponseEntity.ok("Fire alert broadcasted to all users");
        }
    }
}
