package com.pharmacare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for submitting or handling return and cancellation requests.
 */
public class ReturnRequestActionDto {

    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;

    @NotBlank(message = "Reason for return is required")
    private String reason;

    public ReturnRequestActionDto() {
    }

    // Getters and Setters
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}