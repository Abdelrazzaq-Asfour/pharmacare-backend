package com.pharmacare.service.impl;

import com.pharmacare.dto.request.CreateProductRequest;
import com.pharmacare.dto.response.ProductResponse;
import com.pharmacare.mapper.ProductMapper;
import com.pharmacare.model.Product;
import com.pharmacare.repository.ProductBatchRepository;
import com.pharmacare.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Enterprise-grade unit test suite for ProductServiceImpl.
 * Validates business rules, data mapping, and inventory batch dependency constraints.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductBatchRepository productBatchRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Should create product successfully and set active to true")
    void createProduct_ShouldSucceed() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setTradeName("Panadol Extra");
        request.setScientificName("Paracetamol");
        request.setCategory("Analgesics");
        request.setSellingPricePerBox(BigDecimal.valueOf(5.00));
        request.setMinStockAlertThreshold(10);

        Product product = new Product();
        Product savedProduct = new Product();
        savedProduct.setProductId(1L);

        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setProductId(1L);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.toResponse(savedProduct)).thenReturn(expectedResponse);

        // Act
        ProductResponse response = productService.createProduct(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(1L);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toResponse(savedProduct);
    }

    @Test
    @DisplayName("Should return list of products matching search keyword")
    void searchProducts_ShouldReturnList() {
        // Arrange
        String keyword = "Panadol";
        Product product = new Product();
        ProductResponse productResponse = new ProductResponse();

        when(productRepository.searchProducts(keyword)).thenReturn(List.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        // Act
        List<ProductResponse> results = productService.searchProducts(keyword);

        // Assert
        assertThat(results).hasSize(1);
        verify(productRepository, times(1)).searchProducts(keyword);
    }

    @Test
    @DisplayName("Should update product price successfully when product exists")
    void updateProductPrice_ShouldSucceed() {
        // Arrange
        Long productId = 1L;
        BigDecimal newPrice = BigDecimal.valueOf(7.50);
        Product product = new Product();
        product.setProductId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(new ProductResponse());

        // Act
        ProductResponse response = productService.updateProductPrice(productId, newPrice);

        // Assert
        assertThat(response).isNotNull();
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Should throw exception when updating price for non-existent product")
    void updateProductPrice_ShouldThrowExceptionWhenNotFound() {
        // Arrange
        Long productId = 99L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.updateProductPrice(productId, BigDecimal.TEN))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found with ID: " + productId);

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update full product details successfully")
    void updateProduct_ShouldSucceed() {
        // Arrange
        Long productId = 1L;
        CreateProductRequest request = new CreateProductRequest();
        request.setTradeName("Updated Name");

        Product existingProduct = new Product();
        existingProduct.setProductId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
        when(productMapper.toResponse(existingProduct)).thenReturn(new ProductResponse());

        // Act
        ProductResponse response = productService.updateProduct(productId, request);

        // Assert
        assertThat(response).isNotNull();
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    @DisplayName("Should perform soft-delete (set active to false) when inventory batches exist")
    void deleteProduct_ShouldSoftDeleteWhenBatchesExist() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setProductId(productId);
        product.setActive(true);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productBatchRepository.existsByProduct_ProductId(productId)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        productService.deleteProduct(productId);

        // Assert
        assertThat(product.isActive()).isFalse();
        verify(productRepository, times(1)).save(product);
        verify(productRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should perform hard-delete when no inventory batches are linked")
    void deleteProduct_ShouldHardDeleteWhenNoBatchesExist() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setProductId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productBatchRepository.existsByProduct_ProductId(productId)).thenReturn(false);

        // Act
        productService.deleteProduct(productId);

// Assert
        verify(productRepository, times(1)).delete(product);
        verify(productRepository, never()).save(any());
    }
}