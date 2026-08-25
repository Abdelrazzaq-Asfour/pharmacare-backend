package com.pharmacare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Tracks manual inventory adjustments (damaged goods, expirations, stock audits).
 */
@Entity
@Table(name = "stock_adjustments", indexes = {
        @Index(name = "idx_batch_id", columnList = "batch_id")
})
public class StockAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adjustment_id")
    private Long adjustmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id", nullable = false)
    private ProductBatch batch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "adjustment_type", nullable = false, length = 30)
    private AdjustmentType adjustmentType;

    @Column(name = "quantity_changed", nullable = false)
    private Integer quantityChanged; // Negative for loss, positive for addition

    @Column(name = "reason", nullable = false, length = 255)
    private String reason;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum AdjustmentType { DAMAGED, EXPIRED, CORRECTION }

    public StockAdjustment() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getAdjustmentId() { return adjustmentId; }
    public void setAdjustmentId(Long adjustmentId) { this.adjustmentId = adjustmentId; }

    public ProductBatch getBatch() { return batch; }
    public void setBatch(ProductBatch batch) { this.batch = batch; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public AdjustmentType getAdjustmentType() { return adjustmentType; }
    public void setAdjustmentType(AdjustmentType adjustmentType) { this.adjustmentType = adjustmentType; }

    public Integer getQuantityChanged() { return quantityChanged; }
    public void setQuantityChanged(Integer quantityChanged) { this.quantityChanged = quantityChanged; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}