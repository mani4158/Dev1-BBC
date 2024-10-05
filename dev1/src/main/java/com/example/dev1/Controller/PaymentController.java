package com.example.dev1.Controller;
import com.example.dev1.Model.Transaction;
import com.example.dev1.Service.TransactionService;
import com.lowagie.text.DocumentException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {
    @Autowired
    private TransactionService transactionService;
    @PostMapping("/manual-payment")
    public ResponseEntity<Transaction> makeManualPayment(@RequestParam long invoice_Id, @RequestParam String paymentMethod) throws MessagingException, DocumentException {
        System.out.println("Hellow");
        Transaction transaction = transactionService.createTransaction(  invoice_Id, paymentMethod);
        return ResponseEntity.ok(transaction);
    }
    @GetMapping("/transactions/customer/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByCustomerId(@PathVariable String customerId) {
        String stringId = String.valueOf(customerId);
        List<Transaction> transactions = transactionService.getTransactionsByCustomerId(stringId    );
        return ResponseEntity.ok(transactions);
    }
    @GetMapping("/transactions/customer/allval")
    public ResponseEntity<List<Transaction>> getTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}
