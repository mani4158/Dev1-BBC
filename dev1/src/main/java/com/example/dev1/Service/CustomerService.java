//package com.example.dev1.Service;
//import com.example.dev1.DTO.CustomerRequest;
//import com.example.dev1.Model.Customer;
//import com.example.dev1.Model.Invoice;
//import com.example.dev1.Repository.CustomerRepository;
//import com.example.dev1.Repository.InvoiceRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.springframework.http.ResponseEntity.ok;
//
//@Service
//public class CustomerService {
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private InvoiceRepository invoiceRepository;
//
//    public ResponseEntity<?> addorUpdateCustomer(CustomerRequest customerRequest) {
//        Customer existingCustomer=customerRepository.findByCustId(customerRequest.getCustId());
//        Invoice invoice=new Invoice();
//        if(existingCustomer==null)
//        {
//        Customer customer=new Customer();
//        customer.setCustId(customerRequest.getCustId());
//        customer.setEmail(customerRequest.getEmail());
//        customer.setPhoneNumber(customerRequest.getPhoneNumber());
//        customer.setName(customerRequest.getName());
//        invoice.setCustomer(customer);
//       existingCustomer= customerRepository.save(customer);
//        }
//
//        invoice.setAmountStatus(false);
//        invoice.setDueDate(customerRequest.getDueDate());
//        invoice.setStartDate(customerRequest.getStartDate());
//        invoice.setEndDate(customerRequest.getEndDate());
//        invoice.setUnitsConsumed(customerRequest.getUnitsConsumed());
//        invoice.setTotalAmount(customerRequest.getUnitsConsumed()*41.50);
//        invoice.setCustomer(existingCustomer);
//        invoiceRepository.save(invoice);
//        return ResponseEntity.ok(invoice);
//    }
//    public ResponseEntity<?> bulkUploadCustomers(MultipartFile file) {
//        List<Customer> customers = new ArrayList<>();
//        List<Invoice> invoices = new ArrayList<>();
//        String[] headers = null;
//
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            String line;
//            if ((line = br.readLine()) != null) {
//                headers = line.split(",");  // Store the headers
//            }
//            while ((line = br.readLine()) != null) {
//               String[] data = line.split(",");
//                CustomerRequest customerRequest = new CustomerRequest();
//                System.out.println("CustId:"+data[0].trim());
//                customerRequest.setCustId(data[0].trim());
//                customerRequest.setName(data[1].trim());
//                customerRequest.setEmail(data[2].trim());
//                customerRequest.setPhoneNumber(data[3].trim());
//                customerRequest.setUnitsConsumed(Double.parseDouble(data[4].trim()));
//                customerRequest.setStartDate(LocalDate.parse(data[5].trim()));
//                customerRequest.setEndDate(LocalDate.parse(data[6].trim()));
//                customerRequest.setDueDate(LocalDate.parse(data[7].trim()));
//                addorUpdateCustomer(customerRequest);
//            }
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
//        }
//
//        return ResponseEntity.ok("Bulk upload successful!");
//    }
//
//
//
//}
//
//
package com.example.dev1.Service;

import com.example.dev1.DTO.CustomerRequest;
import com.example.dev1.Model.Customer;
import com.example.dev1.Model.Invoice;
import com.example.dev1.Repository.CustomerRepository;
import com.example.dev1.Repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private JavaMailSender mailSender;

    // Method to add or update a customer
    public ResponseEntity<?> addorUpdateCustomer(CustomerRequest customerRequest) {
        // Check if the customer already exists
        System.out.println(customerRequest.toString());
        Customer existingCustomer = customerRepository.findByCustId(customerRequest.getCustId());
        Invoice invoice = new Invoice();
        boolean isNewCustomer = false;
        System.out.println(existingCustomer);
        Customer customer = new Customer();
        if (existingCustomer == null) {
            isNewCustomer = true;  // Mark the customer as new
            customer.setCustId(String.valueOf(customerRequest.getCustId()));
            customer.setEmail(customerRequest.getEmail());
            customer.setPhoneNumber(customerRequest.getPhoneNumber());
            customer.setName(customerRequest.getName());
//            customer.setId((long)3);
            System.out.println("customer "+customer.getCustId());
            existingCustomer = customerRepository.save(customer);
            System.out.println("hello");// Save the new customer
        }
        System.out.println(existingCustomer);
        // Generate or update the invoice for the customer
        invoice.setAmountStatus(false);
        invoice.setDueDate(customerRequest.getDueDate());
        invoice.setStartDate(customerRequest.getStartDate());
        invoice.setEndDate(customerRequest.getEndDate());
        invoice.setUnitsConsumed(customerRequest.getUnitsConsumed());
        invoice.setTotalAmount(customerRequest.getUnitsConsumed() * 41.50);  // Total amount calculation
        invoice.setCustomer(existingCustomer);

        invoiceRepository.save(invoice);

        System.out.println("after invoice");
        // Save the invoice
        // Send the email if it's a new customer
        if (isNewCustomer) {
            sendCustomerIdEmail(existingCustomer.getEmail(), existingCustomer.getCustId()+"");
        }

        return ResponseEntity.ok(invoice);
    }

    // Method to send the customer ID email
    private void sendCustomerIdEmail(String toEmail, String customerId) {
        String subject = "Your Customer ID - BBC Utility Billing";
        String messageBody = "Dear Customer,\n\n" +
                "Your customer account has been successfully created. " +
                "Your customer ID is: " + customerId + ".\n\n" +
                "Thank you for choosing BBC Utility Billing.\n\n" +
                "Best regards,\n" +
                "BBC Utility Billing Team";

        // Creating the email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(messageBody);

        // Attempt to send the email
        try {
            mailSender.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (Exception e) {
            System.err.println("Error while sending email: " + e.getMessage());
        }
    }

    // Bulk upload method for customers from a CSV file
    public ResponseEntity<?> bulkUploadCustomers(MultipartFile file) {
        List<String> errorMessages = new ArrayList<>();

        // Check if the uploaded file is a CSV
        if (!file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type. Please upload a CSV file.");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            System.out.println("inside bulk");
            // Read the first line for headers (if necessary)
            br.readLine();  // Skip the headers
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Read and process the rest of the CSV data
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                // Validate the CSV data
                if (data.length < 8) {
                    errorMessages.add("Row " + line + " has missing fields.");
                    continue;
                }

                // Validate fields
                String custId = data[0].trim();
                String name = data[1].trim();
                String email = data[2].trim();
                String phoneNumber = data[3].trim();
                String unitsConsumedStr = data[4].trim();
                String startDateStr = data[5].trim();
                String endDateStr = data[6].trim();
                String dueDateStr = data[7].trim();

                // Validate mobile number (10 digits)
                if (!Pattern.matches("^[0-9]{10}$", phoneNumber)) {
                    errorMessages.add("Invalid phone number for customer ID " + custId + ": " + phoneNumber);
                    continue;
                }

                // Validate email format
                if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
                    errorMessages.add("Invalid email for customer ID " + custId + ": " + email);
                    continue;
                }

                // Validate units consumed
                double unitsConsumed;
                try {
                    unitsConsumed = Double.parseDouble(unitsConsumedStr);
                    if (unitsConsumed <= 0) {
                        errorMessages.add("Units consumed must be a positive number for customer ID " + custId);
                        continue;
                    }
                } catch (NumberFormatException e) {
                    errorMessages.add("Invalid units consumed for customer ID " + custId + ": " + unitsConsumedStr);
                    continue;
                }

                // Parse dates and validate
                LocalDate startDate, endDate, dueDate;
                try {
                    startDate = LocalDate.parse(startDateStr, formatter);
                    endDate = LocalDate.parse(endDateStr, formatter);
                    dueDate = LocalDate.parse(dueDateStr, formatter);
                } catch (DateTimeParseException e) {
                    errorMessages.add("Invalid date format for customer ID " + custId + ": " + e.getMessage());
                    continue;
                }

                // Add or update the customer if no errors
                CustomerRequest customerRequest = new CustomerRequest();
                customerRequest.setCustId(custId);
                customerRequest.setName(name);
                customerRequest.setEmail(email);
                customerRequest.setPhoneNumber(phoneNumber + "+91");
                customerRequest.setUnitsConsumed(unitsConsumed);
                customerRequest.setStartDate(startDate);
                customerRequest.setEndDate(endDate);
                customerRequest.setDueDate(dueDate);
                addorUpdateCustomer(customerRequest);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }

        // Return error messages if any were collected
        if (!errorMessages.isEmpty()) {
            return ResponseEntity.badRequest().body("Please Give valid data check Your CSV File" );
        }

        return ResponseEntity.ok("Bulk upload successful!");
    }
}
