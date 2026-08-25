package com.pharmacare.dto.response;

import java.math.BigDecimal;

/**
 * Response DTO for product catalog data (Boxes Only).
 */
public class ProductResponse {

    private Long productId;
    private String tradeName;
    private String scientificName;
    private String category;
    private BigDecimal sellingPricePerBox;
    private Integer minStockAlertThreshold;
    private boolean isActive;

    public ProductResponse() {
    }

    // Getters and Setters (Updated for Boxes)
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getTradeName() { return tradeName; }
    public void setTradeName(String tradeName) { this.tradeName = tradeName; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getSellingPricePerBox() { return sellingPricePerBox; }
    public void setSellingPricePerBox(BigDecimal sellingPricePerBox) { this.sellingPricePerBox = sellingPricePerBox; }

    public Integer getMinStockAlertThreshold() { return minStockAlertThreshold; }
    public void setMinStockAlertThreshold(Integer minStockAlertThreshold) { this.minStockAlertThreshold = minStockAlertThreshold; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}