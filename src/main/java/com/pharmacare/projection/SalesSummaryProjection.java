package com.pharmacare.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * High-performance projection for financial sales reporting and cashier audits.
 */
public interface SalesSummaryProjection {
    Long getInvoiceId();
    String getInvoiceNumber();
    String getPharmacistUsername();
    BigDecimal getTotalAmount();
    String getPaymentMethod();
    String getInvoiceStatus();
    LocalDateTime getCreatedAt();
}