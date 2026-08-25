package com.pharmacare.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class CreateProductRequest {

    @NotBlank(message = "Trade name is required")
    private String tradeName;

    @NotBlank(message = "Scientific name is required")
    private String scientificName;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Selling price per box is required")
    @Min(value = 0, message = "Selling price must be greater than or equal to 0")
    private BigDecimal sellingPricePerBox;

    @NotNull(message = "Min stock alert threshold is required")
    private Integer minStockAlertThreshold;

    public CreateProductRequest() {
    }

    // Getters and Setters
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
}