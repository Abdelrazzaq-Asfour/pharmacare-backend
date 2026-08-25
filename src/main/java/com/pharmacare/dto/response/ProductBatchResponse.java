package com.pharmacare.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductBatchResponse {
    private Long batchId;
    private String batchNumber;
    private Long productId;
    private String productName;
    private String supplier;
    private int currentBoxesQuantity;
    private BigDecimal costPerBox;
    private LocalDate expirationDate;
}