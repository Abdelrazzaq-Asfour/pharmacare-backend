package com.pharmacare.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StockIntakeRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotBlank(message = "Batch number is required")
    private String batchNumber;

    @NotNull(message = "Cost price per box is required")
    @Min(value = 0, message = "Cost price must be greater than or equal to 0")
    private BigDecimal costPricePerBox;

    @NotNull(message = "Initial boxes quantity is required")
    @Min(value = 1, message = "Initial boxes quantity must be at least 1")
    private Integer initialBoxesQuantity;

    @NotNull(message = "Expiration date is required")
    private LocalDate expirationDate;

    public StockIntakeRequest() {
    }

    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public BigDecimal getCostPricePerBox() { return costPricePerBox; }
    public void setCostPricePerBox(BigDecimal costPricePerBox) { this.costPricePerBox = costPricePerBox; }

    public Integer getInitialBoxesQuantity() { return initialBoxesQuantity; }
    public void setInitialBoxesQuantity(Integer initialBoxesQuantity) { this.initialBoxesQuantity = initialBoxesQuantity; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
}