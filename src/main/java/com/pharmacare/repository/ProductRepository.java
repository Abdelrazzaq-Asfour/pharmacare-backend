package com.pharmacare.repository;

import com.pharmacare.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for managing product catalogs and searching medicines.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Secure search query supporting trade name and scientific name partial matching
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND (LOWER(p.tradeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.scientificName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchProducts(@Param("keyword") String keyword);
}