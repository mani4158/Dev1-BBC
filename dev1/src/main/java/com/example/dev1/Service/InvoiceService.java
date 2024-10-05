package com.example.dev1.Service;

import com.example.dev1.Model.Invoice;
import com.example.dev1.Repository.InvoiceRepository;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dev1.Model.Invoice;
import com.example.dev1.Model.Customer;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }
    public List<Invoice> getInvoicesByCustomerId(String customerId) {
        return invoiceRepository.findByCustomer_CustId(customerId);
    }
    public Optional<Invoice> getInvoiceById(Long Id) {
        return invoiceRepository.findById(Id);
    }

    public Invoice updateInvoiceForManualPayment(Long invoiceId, String paymentMethod) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (LocalDate.now().isBefore(invoice.getDueDate())) {
            double discount = invoice.getTotalAmount() * 0.05;
            invoice.setEarlyDiscount(discount);
            invoice.setFinal_Amount(invoice.getTotalAmount() - discount);
        }
         else{
             invoice.setFinal_Amount(invoice.getTotalAmount());
        }
         
        invoice.setAmountStatus(true);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setPayDate(LocalDate.now());

        return invoiceRepository.save(invoice);
    }


    // Method to generate PDF for the invoice
    public ByteArrayInputStream generateInvoicePdf(Long invoiceId) {
        Invoice invoice = findInvoiceById(invoiceId);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Currency formatter for INR
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Adding invoice information
            document.addTitle("Invoice");
            document.add(new Paragraph("Invoice Details"));
            document.add(new Paragraph("-------------------------------------------------------------"));

            // Invoice and Customer Information
            document.add(new Paragraph("Invoice ID: " + invoice.getId()));
            Customer customer = invoice.getCustomer();
            document.add(new Paragraph("Customer ID: " + customer.getCustId()));
            document.add(new Paragraph("Customer Name: " + customer.getName()));
            document.add(new Paragraph("Customer Email: " + customer.getEmail()));
            document.add(new Paragraph("Units Consumed: " + invoice.getUnitsConsumed()));
            document.add(new Paragraph("Billing Period: " + invoice.getStartDate() + " to " + invoice.getEndDate()));
            document.add(new Paragraph("Due Date: " + invoice.getDueDate()));
            document.add(new Paragraph("-------------------------------------------------------------"));

            // Discounts and Final Amount
            document.add(new Paragraph("Discounts Applied:"));
            document.add(new Paragraph("Early Payment Discount: " + currencyFormatter.format(invoice.getEarlyDiscount())));
            document.add(new Paragraph("Online Payment Discount: " + currencyFormatter.format(invoice.getOnline_payment_Discount())));
            document.add(new Paragraph("-------------------------------------------------------------"));
            document.add(new Paragraph("Total Amount: " + currencyFormatter.format(invoice.getTotalAmount())));
            document.add(new Paragraph("Final Amount after Discounts: " + currencyFormatter.format(invoice.getFinal_Amount())));
            document.add(new Paragraph("Payment Method: " + invoice.getPaymentMethod()));

            document.close();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Method to find invoice by ID
    public Invoice findInvoiceById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + invoiceId));
    }
}

