package com.example.dev1.Controller;
import com.example.dev1.Model.Employee;
import com.example.dev1.Service.EmployeeService;
import com.example.dev1.Service.OtpService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/employees")
@CrossOrigin
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private OtpService otpService;

    @GetMapping("/check/{employeeId}")
    public ResponseEntity<Boolean> checkEmployeeId(@PathVariable int employeeId) {
        boolean exists = employeeService.checkEmployeeIdExists(employeeId);
        return ResponseEntity.ok(exists);
    }
    @PostMapping("/add")
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee) throws MessagingException {
        employeeService.addEmployee(employee);
        return ResponseEntity.ok("Employee added successfully");
    }
    @PostMapping("/generate-otp")
    public ResponseEntity<Map<String, String>> generateOtp(@RequestBody Map<String, Integer> request) {
        Integer employeeId = request.get("employeeId");
        if (employeeId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Employee ID must be provided"));
        }
        try {
            System.out.println("mani");
            String message = employeeService.generateOtpForEmployee(employeeId);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid Employee ID"));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error sending OTP"));
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> request) {
        String otp = request.get("otp");
        if (otp == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "OTP must be provided"));
        }
        if (otpService.verifyOtp(otp)) {
            return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid OTP"));
        }
    }
}
