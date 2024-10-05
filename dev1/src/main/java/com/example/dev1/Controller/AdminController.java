package com.example.dev1.Controller;

import com.example.dev1.Service.AdminService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")  // Allow Angular app access
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (adminService.validateAdmin(username, password)) {
            return ResponseEntity.ok().body(Map.of("message", "Login successful"));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
    @PostMapping("/request-otp")
    public ResponseEntity<Map<String, String>> requestOtp(@RequestBody Map<String, String> request) throws Exception {
        //String email = request.get("email");
        adminService.generateAndSendOtp();

        return ResponseEntity.ok().body(Map.of("message", "OTP sent to email"));
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<Map<String, String>> validateOtp(@RequestBody Map<String, String> request) {
        String otp = request.get("otp");

        if (adminService.validateOtp(otp)) {
            return ResponseEntity.ok().body(Map.of("message", "OTP validated"));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid OTP")); 
        }
    }
}
