package com.example.dev1.Service;

import com.example.dev1.Model.Employee;
import com.example.dev1.Repository.EmployeeRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
     EmployeeRepository employeeRepository;
    @Autowired
    private OtpService otpService;
    public void addEmployee(Employee employee) throws MessagingException {
        employeeRepository.save(employee);
        Employee savedEmployee = employeeRepository.save(employee);
        int employeeId = savedEmployee.getEmployeeId();
        String email = savedEmployee.getEmail();
        sendEmployeeIdEmail(email, employeeId);
    }
    private void sendEmployeeIdEmail(String email, int employeeId) throws MessagingException {
        // Craft the email content
        String subject = "Your Employee ID";
        String message = "Dear Employee,\n\nYour Employee ID is: " + employeeId + ".\nPlease use this ID for further correspondence.\n\nBest Regards,\nCompany";

        // Use OtpService or your email service to send the email
        otpService.sendEmail12(email, subject, message);
    }

    public boolean checkEmployeeIdExists(int employeeId) {
        return employeeRepository.findByEmployeeId(employeeId).isPresent();
    }
    public String generateOtpForEmployee(int employeeId) throws MessagingException {
        Optional<Employee> employeeOptional = employeeRepository.findByEmployeeId(employeeId);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            String email = employee.getEmail();
            if (!isValidEmail(email)) {
                throw new IllegalArgumentException("Invalid email address for employee ID: " + employeeId);
            }
            String otp = otpService.generateOtp(employeeId);  // Pass employee ID to generate OTP
            otpService.sendOtpEmail(email, otp);
            return "OTP sent to " + email;
        } else {
            // Handle invalid employee ID
            throw new IllegalArgumentException("Invalid Employee ID: " + employeeId);
        }
    }

    public boolean validateOtp(String otp) {
        return otpService.verifyOtp(otp);
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && email.contains("@");
    }
}
