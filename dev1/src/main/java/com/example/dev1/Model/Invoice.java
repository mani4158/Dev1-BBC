package com.example.dev1.Model;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "Bills")
public class Invoice {
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getUnitsConsumed() {
        return unitsConsumed;
    }
    public void setUnitsConsumed(double unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public boolean getAmountStatus() {
        return amountStatus;
    }
    public void setAmountStatus(boolean amountStatus) {
        this.amountStatus = amountStatus;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "cust_id",nullable = false)
    private Customer customer;
    private double unitsConsumed;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;
    private LocalDate dueDate;
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private String  paymentMethod;
    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public double getEarlyDiscount() {
        return earlyDiscount;
    }

    public void setEarlyDiscount(double earlyDiscount) {
        this.earlyDiscount = earlyDiscount;
    }

    public double getOnline_payment_Discount() {
        return online_payment_Discount;
    }

    public void setOnline_payment_Discount(double online_payment_Discount) {
        this.online_payment_Discount = online_payment_Discount;
    }

    public double getFinal_Amount() {
        return final_Amount;
    }

    public void setFinal_Amount(double final_Amount) {
        this.final_Amount = final_Amount;
    }

    public boolean isAmountStatus() {
        return amountStatus;
    }

    private LocalDate payDate;
    private  double earlyDiscount;
    private double online_payment_Discount;
    private double final_Amount;

    private boolean amountStatus;

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}




