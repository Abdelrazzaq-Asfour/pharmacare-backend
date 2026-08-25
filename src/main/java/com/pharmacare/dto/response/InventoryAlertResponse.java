package com.pharmacare.dto.response;

/**
 * Response DTO for products falling below threshold or approaching expiration.
 */
public class InventoryAlertResponse {

    private Long productId;
    private String tradeName;
    private Integer totalAvailableBaseQuantity;
    private Integer alertThreshold;
    private String alertType; // LOW_STOCK or NEAR_EXPIRY

    public InventoryAlertResponse(Long productId, String tradeName, Integer totalAvailableBaseQuantity, Integer alertThreshold, String alertType) {
        this.productId = productId;
        this.tradeName = tradeName;
        this.totalAvailableBaseQuantity = totalAvailableBaseQuantity;
        this.alertThreshold = alertThreshold;
        this.alertType = alertType;
    }

    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getTradeName() { return tradeName; }
    public void setTradeName(String tradeName) { this.tradeName = tradeName; }

    public Integer getTotalAvailableBaseQuantity() { return totalAvailableBaseQuantity; }
    public void setTotalAvailableBaseQuantity(Integer totalAvailableBaseQuantity) { this.totalAvailableBaseQuantity = totalAvailableBaseQuantity; }

    public Integer getAlertThreshold() { return alertThreshold; }
    public void setAlertThreshold(Integer alertThreshold) { this.alertThreshold = alertThreshold; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
}