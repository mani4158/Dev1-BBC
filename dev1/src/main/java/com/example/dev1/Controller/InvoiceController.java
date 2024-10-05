package com.example.dev1.Controller;

import com.example.dev1.Model.Invoice;
import com.example.dev1.Repository.InvoiceRepository;
import com.example.dev1.Service.InvoiceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:4200")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @GetMapping("/customer/{id}")
    public ResponseEntity<List<Invoice>> getInvoicesByCustomerId(@PathVariable String id) {
        List<Invoice> invoices = invoiceService.getInvoicesByCustomerId(id);
        if (invoices.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoices);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice>invoices1=invoiceRepository.findAll();
//        if (invoices1.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        return ResponseEntity.ok(invoices1);
    }
//    @GetMapping("/invoice/{id}")
//    public ResponseEntity<Optional<Invoice>> getInvoicesById(@PathVariable Long id) {
//        Optional<Invoice> invoices = invoiceService.getInvoiceById(id);
//        if (invoices.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(invoices);
//    }
@GetMapping("/invoice/{id}")
public ResponseEntity<?> getInvoicesById(@PathVariable Long id) {
    try {
        // Fetch the invoice using the service method
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);

        // If the invoice is not found, return 404 Not Found
        if (invoice.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invoice with ID " + id + " not found.");
        }

        // If the invoice is found, return it with 200 OK status
        return ResponseEntity.ok(invoice);

    } catch (EntityNotFoundException ex) {
        // Handle the case where the related entity (e.g., Customer) is not found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + ex.getMessage());
    } catch (Exception ex) {
        // Catch other potential exceptions and return a generic error response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}

    @GetMapping("/download/{invoiceId}")
    public ResponseEntity<InputStreamResource> downloadInvoice(@PathVariable Long invoiceId) {
        ByteArrayInputStream bis = invoiceService.generateInvoicePdf(invoiceId);
        System.out.println("InvoiceId:"+invoiceId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice_" + invoiceId + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}

