package com.example.dev1.Service;
import com.example.dev1.Model.Admin;
import com.example.dev1.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmailService emailService;  // To send emails

    private String generatedOtp;  // In-memory storage of OTP for validation

    public boolean validateAdmin(String username, String password) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        return adminOpt.isPresent() && adminOpt.get().getPassword().equals(password);
    }

    public void generateAndSendOtp() throws Exception {

        Random random = new Random();
        generatedOtp = String.format("%04d", random.nextInt(10000));  // Generate 4-digit OTP

        emailService.sendOtpEmail("manikandand24cse@srishakthi.ac.in", generatedOtp);
        System.out.print(generatedOtp); // Log or handle the generated OTP as needed
    }

    public boolean validateOtp(String otp) {
        System.out.println(otp+" "+generatedOtp);
        return generatedOtp != null && generatedOtp.equals(otp);
    }


}
