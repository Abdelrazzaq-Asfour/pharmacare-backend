package com.pharmacare.controller;

import com.pharmacare.dto.request.StockIntakeRequest;
import com.pharmacare.dto.response.InventoryAlertResponse;
import com.pharmacare.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing supply chain intake, batch registrations, and low stock monitoring.
 */
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/intake")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<Void> registerStockIntake(@Valid @RequestBody StockIntakeRequest request) {
        inventoryService.registerStockIntake(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/alerts")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'INVENTORY_CLERK')")
    public ResponseEntity<List<InventoryAlertResponse>> getInventoryAlerts() {
        List<InventoryAlertResponse> alerts = inventoryService.getInventoryAlerts();
        return ResponseEntity.ok(alerts);
    }

    @PostMapping("/adjustments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> recordAdjustment(@RequestParam Long batchId,
                                                 @RequestParam Long userId,
                                                 @RequestParam String type,
                                                 @RequestParam int qtyChanged,
                                                 @RequestParam String reason) {
        inventoryService.recordStockAdjustment(batchId, userId, type, qtyChanged, reason);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/batches")
    public ResponseEntity<List<com.pharmacare.dto.response.ProductBatchResponse>> getAllBatches() {
        List<com.pharmacare.dto.response.ProductBatchResponse> batches = inventoryService.getAllBatches();
        return ResponseEntity.ok(batches);
    }

}