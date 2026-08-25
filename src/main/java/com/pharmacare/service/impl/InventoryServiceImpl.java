package com.pharmacare.service.impl;

import com.pharmacare.dto.request.StockIntakeRequest;
import com.pharmacare.dto.response.InventoryAlertResponse;
import com.pharmacare.dto.response.ProductBatchResponse;
import com.pharmacare.model.*;
import com.pharmacare.repository.*;
import com.pharmacare.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final ProductBatchRepository batchRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final StockAdjustmentRepository adjustmentRepository;

    public InventoryServiceImpl(ProductBatchRepository batchRepository,
                                ProductRepository productRepository,
                                SupplierRepository supplierRepository,
                                UserRepository userRepository,
                                StockAdjustmentRepository adjustmentRepository) {
        this.batchRepository = batchRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.adjustmentRepository = adjustmentRepository;
    }

    @Override
    @Transactional
    public void registerStockIntake(StockIntakeRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        ProductBatch batch = new ProductBatch();
        batch.setProduct(product);
        batch.setSupplier(supplier);
        batch.setBatchNumber(request.getBatchNumber());
        batch.setCostPricePerBox(request.getCostPricePerBox());
        batch.setInitialBoxesQuantity(request.getInitialBoxesQuantity());
        batch.setCurrentBoxesQuantity(request.getInitialBoxesQuantity());
        batch.setExpirationDate(request.getExpirationDate());

        batchRepository.save(batch);
    }

    @Override
    @Transactional
    public void recordStockAdjustment(Long batchId, Long userId, String adjustmentTypeStr, int quantityChanged, String reason) {
        ProductBatch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int newQty = batch.getCurrentBoxesQuantity() + quantityChanged;
        if (newQty < 0) {
            throw new RuntimeException("Adjustment results in negative stock level.");
        }

        batch.setCurrentBoxesQuantity(newQty);
        batchRepository.save(batch);

        StockAdjustment adjustment = new StockAdjustment();
        adjustment.setBatch(batch);
        adjustment.setUser(user);
        adjustment.setAdjustmentType(StockAdjustment.AdjustmentType.valueOf(adjustmentTypeStr));
        adjustment.setQuantityChanged(quantityChanged);
        adjustment.setReason(reason);

        adjustmentRepository.save(adjustment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryAlertResponse> getInventoryAlerts() {
        var lowStockProjections = batchRepository.getLowStockAlerts();
        List<InventoryAlertResponse> alerts = new ArrayList<>();

        for (var p : lowStockProjections) {
            alerts.add(new InventoryAlertResponse(
                    p.getProductId(),
                    p.getTradeName(),
                    p.getTotalAvailableBaseQuantity().intValue(),
                    p.getMinStockAlertThreshold(),
                    "LOW_STOCK"
            ));
        }
        return alerts;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductBatchResponse> getAllBatches() {
        List<ProductBatch> batches = batchRepository.findAll();
        return batches.stream().map(batch -> {
            ProductBatchResponse res = new ProductBatchResponse();
            res.setBatchId(batch.getBatchId());
            res.setBatchNumber(batch.getBatchNumber());
            res.setProductId(batch.getProduct() != null ? batch.getProduct().getProductId() : null);
            res.setProductName(batch.getProduct() != null ? batch.getProduct().getTradeName() : "N/A");
            res.setSupplier(batch.getSupplier() != null ? batch.getSupplier().getSupplierName() : "N/A");
            res.setCurrentBoxesQuantity(batch.getCurrentBoxesQuantity());
            res.setCostPerBox(batch.getCostPricePerBox());
            res.setExpirationDate(batch.getExpirationDate());
            return res;
        }).collect(Collectors.toList());
    }
}