package com.pharmacare.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ProductBatch entity representing inventory shipments and FIFO tracking (Boxes Only).
 */
@Entity
@Table(name = "product_batches", indexes = {
        @Index(name = "idx_fifo_lookup", columnList = "product_id, expiration_date, current_boxes_quantity")
})
public class ProductBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Long batchId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "batch_number", nullable = false, length = 100)
    private String batchNumber;

    @Column(name = "cost_price_per_box", nullable = false, precision = 10, scale = 2)
    private BigDecimal costPricePerBox;

    @Column(name = "initial_boxes_quantity", nullable = false)
    private Integer initialBoxesQuantity;

    @Column(name = "current_boxes_quantity", nullable = false)
    private Integer currentBoxesQuantity;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "received_at", updatable = false)
    private LocalDateTime receivedAt;

    public ProductBatch() {
    }

    @PrePersist
    protected void onReceive() {
        this.receivedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public BigDecimal getCostPricePerBox() { return costPricePerBox; }
    public void setCostPricePerBox(BigDecimal costPricePerBox) { this.costPricePerBox = costPricePerBox; }

    public Integer getInitialBoxesQuantity() { return initialBoxesQuantity; }
    public void setInitialBoxesQuantity(Integer initialBoxesQuantity) { this.initialBoxesQuantity = initialBoxesQuantity; }

    public Integer getCurrentBoxesQuantity() { return currentBoxesQuantity; }
    public void setCurrentBoxesQuantity(Integer currentBoxesQuantity) { this.currentBoxesQuantity = currentBoxesQuantity; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public LocalDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }
}