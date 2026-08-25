package com.pharmacare.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for completed POS sales invoices (Boxes Only).
 */
public class InvoiceResponse {

    private Long invoiceId;
    private String invoiceNumber;
    private String pharmacistUsername;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String invoiceStatus;
    private LocalDateTime createdAt;
    private List<InvoiceItemResponse> items;

    public static class InvoiceItemResponse {
        private String tradeName;
        private String batchNumber;
        private Integer quantityBoxes;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;

        public InvoiceItemResponse() {
        }

        // Getters & Setters
        public String getTradeName() { return tradeName; }
        public void setTradeName(String tradeName) { this.tradeName = tradeName; }

        public String getBatchNumber() { return batchNumber; }
        public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

        public Integer getQuantityBoxes() { return quantityBoxes; }
        public void setQuantityBoxes(Integer quantityBoxes) { this.quantityBoxes = quantityBoxes; }

        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

        public BigDecimal getTotalPrice() { return totalPrice; }
        public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    }

    public InvoiceResponse() {
    }

    // Getters and Setters
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getPharmacistUsername() { return pharmacistUsername; }
    public void setPharmacistUsername(String pharmacistUsername) { this.pharmacistUsername = pharmacistUsername; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getInvoiceStatus() { return invoiceStatus; }
    public void setInvoiceStatus(String invoiceStatus) { this.invoiceStatus = invoiceStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<InvoiceItemResponse> getItems() { return items; }
    public void setItems(List<InvoiceItemResponse> items) { this.items = items; }
}