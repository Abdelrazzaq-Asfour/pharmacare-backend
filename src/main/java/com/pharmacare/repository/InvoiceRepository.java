package com.pharmacare.repository;

import com.pharmacare.model.Invoice;
import com.pharmacare.projection.SalesSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for POS sales invoices and audit trails.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    // High-performance projection query for sales reports within a date range
    @Query(value = "SELECT i.invoice_id AS invoiceId, i.invoice_number AS invoiceNumber, " +
            "u.username AS pharmacistUsername, i.total_amount AS totalAmount, " +
            "i.payment_method AS paymentMethod, i.invoice_status AS invoiceStatus, " +
            "i.created_at AS createdAt " +
            "FROM invoices i JOIN users u ON i.pharmacist_user_id = u.user_id " +
            "WHERE i.created_at BETWEEN :startDate AND :endDate " +
            "ORDER BY i.created_at DESC",
            nativeQuery = true)
    List<SalesSummaryProjection> getSalesSummaryReport(@Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);
}