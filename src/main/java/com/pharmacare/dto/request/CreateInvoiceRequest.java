package com.pharmacare.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO for POS sales processing containing line items (Boxes Only).
 */
public class CreateInvoiceRequest {

    @NotNull(message = "Payment method is required")
    private String paymentMethod; // CASH or CARD

    @NotEmpty(message = "Invoice must contain at least one item")
    @Valid
    private List<InvoiceItemRequest> items;

    public static class InvoiceItemRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @jakarta.validation.constraints.Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantityBoxes;

        public InvoiceItemRequest() {
        }

        // Getters & Setters (Updated for Boxes)
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public Integer getQuantityBoxes() { return quantityBoxes; }
        public void setQuantityBoxes(Integer quantityBoxes) { this.quantityBoxes = quantityBoxes; }
    }

    public CreateInvoiceRequest() {
    }

    // Getters and Setters
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public List<InvoiceItemRequest> getItems() { return items; }
    public void setItems(List<InvoiceItemRequest> items) { this.items = items; }
}