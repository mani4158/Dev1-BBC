package com.example.dev1.Controller;

import com.example.dev1.DTO.CustomerRequest;
import com.example.dev1.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @PostMapping("/add")
    public ResponseEntity<?> addorUpdateCustomer(@RequestBody CustomerRequest customerRequest)
    {
        return customerService.addorUpdateCustomer(customerRequest);
    }
    @PostMapping("/bulk-upload")
    public ResponseEntity<?> bulkUploadCustomers(@RequestParam("file") MultipartFile file) {
        return customerService.bulkUploadCustomers(file);
    }
}
