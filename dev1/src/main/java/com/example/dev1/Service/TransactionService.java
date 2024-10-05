package com.example.dev1.Service;

import com.example.dev1.Model.Customer;
import com.example.dev1.Model.Invoice;
import com.example.dev1.Model.Transaction;
import com.example.dev1.Repository.TransactionRepository;
import com.lowagie.text.DocumentException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private PdfGenerator pdfGenerator;
    public Transaction createTransaction(Long invoiceId, String paymentMethod) throws MessagingException, DocumentException {
        Invoice invoice = invoiceService.updateInvoiceForManualPayment(invoiceId, paymentMethod);

        Transaction transaction = new Transaction();
        transaction.setInvoice(invoice);
        transaction.setCustomer(invoice.getCustomer());  // Set customer from invoice
        transaction.setTransactionDate(LocalDate.now());
        transaction.setPaidAmount(invoice.getFinal_Amount());
        transaction.setPaymentMethod(paymentMethod);
        transaction.setGeneratedAt(LocalDateTime.now());
        transaction.setPaymentStatus("Success");
        transactionRepository.save(transaction);
        ByteArrayOutputStream invoicePdf = pdfGenerator.generateInvoicePdf(transaction);
        // Send the email to the customer
        String customerEmail = transaction.getCustomer().getEmail();
        System.out.println(customerEmail);// Get customer email
        emailService.sendInvoiceEmail(customerEmail, invoicePdf);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByCustomerId(String customerId) {
        return transactionRepository.findByCustomerCustId(customerId);  // Fetch transactions by customer
    }

    public List<Transaction> getAllTransactions() {
        System.out.println(transactionRepository.findAll());
        return  transactionRepository.findAll();
    }
}
