package com.pharmacare.projection;

/**
 * Projection for real-time inventory tracking and threshold breach alerts.
 */
public interface LowStockProjection {
    Long getProductId();
    String getTradeName();
    Long getTotalAvailableBaseQuantity();
    Integer getMinStockAlertThreshold();
}