package com.pharmacare.service;

import com.pharmacare.dto.request.StockIntakeRequest;
import com.pharmacare.dto.response.InventoryAlertResponse;
import com.pharmacare.dto.response.ProductBatchResponse;

import java.util.List;

public interface InventoryService {
    void registerStockIntake(StockIntakeRequest request);
    void recordStockAdjustment(Long batchId, Long userId, String adjustmentType, int quantityChanged, String reason);
    List<InventoryAlertResponse> getInventoryAlerts();
    List<ProductBatchResponse> getAllBatches();//
}