package com.pharmacare.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Core product catalog entity. Stores medicine definitions and global selling prices (Boxes Only).
 */
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_trade_name", columnList = "trade_name"),
        @Index(name = "idx_scientific_name", columnList = "scientific_name")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "trade_name", nullable = false, length = 150)
    private String tradeName;

    @Column(name = "scientific_name", nullable = false, length = 150)
    private String scientificName;

    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @Column(name = "selling_price_per_box", nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPricePerBox;

    @Column(name = "min_stock_alert_threshold", nullable = false)
    private Integer minStockAlertThreshold = 5;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Product() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters & Setters (Updated for Boxes)
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}