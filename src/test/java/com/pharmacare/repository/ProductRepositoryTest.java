package com.pharmacare.repository;

import com.pharmacare.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should return active products matching trade or scientific name keywords securely")
    void searchProducts_ShouldFilterActiveProductsByKeyword() {
        // Arrange: Seed test data with mixed active states and naming patterns
        Product activeProd = new Product();
        activeProd.setTradeName("Panadol Extra");
        activeProd.setScientificName("Paracetamol");
        activeProd.setCategory("Analgesics");
        activeProd.setSellingPricePerBox(BigDecimal.valueOf(5.50));
        activeProd.setMinStockAlertThreshold(10);
        activeProd.setActive(true);
        entityManager.persist(activeProd);

        Product inactiveProd = new Product();
        inactiveProd.setTradeName("Panadol Cold");
        inactiveProd.setScientificName("Paracetamol + Pseudoephedrine");
        inactiveProd.setCategory("Cold");
        inactiveProd.setSellingPricePerBox(BigDecimal.valueOf(7.00));
        inactiveProd.setMinStockAlertThreshold(5);
        inactiveProd.setActive(false); // Should be ignored by security/business rule
        entityManager.persist(inactiveProd);

        entityManager.flush();

        // Act: Execute keyword search matching trade name part
        List<Product> results = productRepository.searchProducts("Panadol");

        // Assert: Ensure only active products are retrieved, ignoring soft-deleted/inactive rows
        assertThat(results)
                .hasSize(1)
                .extracting(Product::getTradeName)
                .containsExactly("Panadol Extra");
    }
}