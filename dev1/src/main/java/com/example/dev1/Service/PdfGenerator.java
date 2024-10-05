package com.example.dev1.Service;
import com.example.dev1.Model.Transaction;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Locale;
@Service
public class PdfGenerator {

    public ByteArrayOutputStream generateInvoicePdf(Transaction transaction) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

         //Add customer and invoice details
        document.add(new Paragraph("Invoice"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Customer Details"));
        document.add(new Paragraph("Name: " + transaction.getCustomer().getName()));
        document.add(new Paragraph("Customer ID: " + transaction.getCustomer().getCustId()));
        document.add(new Paragraph("Email: " + transaction.getCustomer().getEmail()));
        document.add(new Paragraph("Pay date " + transaction.getTransactionDate()));
        document.add(new Paragraph("Transaction Id " + transaction.getId()));

        document.add(new Paragraph(" "));
        // Format currency to INR
        Locale india = new Locale("en", "IN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(india);

        // Create a table for invoice details
        PdfPTable table = new PdfPTable(2);
        table.addCell("Description");
        table.addCell("Amount (INR)");

        // Add invoice details
        table.addCell("Units Consumed");
        table.addCell(String.valueOf(transaction.getInvoice().getUnitsConsumed()));

        table.addCell("Total Amount");
        table.addCell(currencyFormatter.format(transaction.getInvoice().getTotalAmount()));

        table.addCell("Early Payment Discount");
        table.addCell(currencyFormatter.format(transaction.getInvoice().getEarlyDiscount()));

        table.addCell("Online Payment Discount");
        table.addCell(currencyFormatter.format(transaction.getInvoice().getOnline_payment_Discount()));

        table.addCell("Final Amount");
        table.addCell(currencyFormatter.format(transaction.getInvoice().getFinal_Amount()));

        table.addCell("Payment Status");
        table.addCell(transaction.getPaymentStatus());

        // Add table to the document
        document.add(table);

        // Close the document
        document.close();
        return out;
    }
    }

