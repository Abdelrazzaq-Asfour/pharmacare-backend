package com.pharmacare.repository;

import com.pharmacare.model.ProductBatch;
import com.pharmacare.projection.LowStockProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Critical repository handling FIFO batch allocation, expiration tracking, and stock levels (Boxes Only).
 */
@Repository
public interface ProductBatchRepository extends JpaRepository<ProductBatch, Long> {

    // FIFO query: Fetch active batches for a product ordered by expiration date (earliest first)
    @Query("SELECT b FROM ProductBatch b WHERE b.product.productId = :productId AND b.currentBoxesQuantity > 0 ORDER BY b.expirationDate ASC")
    List<ProductBatch> findAvailableBatchesForFifo(@Param("productId") Long productId);

    // Calculate total available boxes quantity across all batches for a specific product
    @Query("SELECT COALESCE(SUM(b.currentBoxesQuantity), 0) FROM ProductBatch b WHERE b.product.productId = :productId")
    Integer getTotalStockByProductId(@Param("productId") Long productId);

    // High-performance projection query to detect low stock items for dashboard alerts
    @Query(value = "SELECT p.product_id AS productId, p.trade_name AS tradeName, " +
            "COALESCE(SUM(b.current_boxes_quantity), 0) AS totalAvailableBaseQuantity, " +
            "p.min_stock_alert_threshold AS minStockAlertThreshold " +
            "FROM products p LEFT JOIN product_batches b ON p.product_id = b.product_id " +
            "WHERE p.is_active = 1 GROUP BY p.product_id " +
            "HAVING totalAvailableBaseQuantity <= p.min_stock_alert_threshold",
            nativeQuery = true)
    List<LowStockProjection> getLowStockAlerts();

    boolean existsByProduct_ProductId(Long productId);
}