package com.pharmacare.repository;

import com.pharmacare.model.StockAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for tracking manual stock adjustments (damage, expiration, audit corrections).
 */
@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Long> {
}