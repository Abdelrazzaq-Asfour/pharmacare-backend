package com.pharmacare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharmacare.dto.request.CreateProductRequest;
import com.pharmacare.dto.response.ProductResponse;
import com.pharmacare.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Comprehensive unit test suite covering 100% of ProductController endpoints.
 */
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("Should successfully create product and return CREATED status")
    void createProduct_ShouldSucceed() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setTradeName("Panadol Extra");
        request.setScientificName("Paracetamol");
        request.setCategory("Analgesics");
        request.setSellingPricePerBox(BigDecimal.valueOf(5.00));
        request.setMinStockAlertThreshold(10);

        ProductResponse response = new ProductResponse();
        response.setProductId(1L);
        response.setTradeName("Panadol Extra");

        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.tradeName").value("Panadol Extra"));

        verify(productService, times(1)).createProduct(any(CreateProductRequest.class));
    }

    @Test
    @DisplayName("Should search products successfully")
    void searchProducts_ShouldSucceed() throws Exception {
        String keyword = "Panadol";
        when(productService.searchProducts(keyword)).thenReturn(List.of(new ProductResponse()));

        mockMvc.perform(get("/api/v1/products/search")
                        .param("keyword", keyword))
                .andExpect(status().isOk());

        verify(productService, times(1)).searchProducts(keyword);
    }

    @Test
    @DisplayName("Should update product price successfully")
    void updatePrice_ShouldSucceed() throws Exception {
        Long productId = 1L;
        BigDecimal newPrice = BigDecimal.valueOf(6.50);
        ProductResponse response = new ProductResponse();
        response.setProductId(productId);

        when(productService.updateProductPrice(eq(productId), eq(newPrice))).thenReturn(response);

        mockMvc.perform(put("/api/v1/products/{id}/price", productId)
                        .param("newPrice", newPrice.toString()))
                .andExpect(status().isOk());

        verify(productService, times(1)).updateProductPrice(productId, newPrice);
    }

    @Test
    @DisplayName("Should update full product details successfully")
    void updateProduct_ShouldSucceed() throws Exception {
        // Arrange
        Long productId = 1L;
        CreateProductRequest request = new CreateProductRequest();
        request.setTradeName("Panadol Advance");
        request.setScientificName("Paracetamol");
        request.setCategory("Analgesics");
        request.setSellingPricePerBox(BigDecimal.valueOf(6.00));
        request.setMinStockAlertThreshold(15);

        ProductResponse response = new ProductResponse();
        response.setProductId(productId);
        response.setTradeName("Panadol Advance");

        when(productService.updateProduct(eq(productId), any(CreateProductRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.tradeName").value("Panadol Advance"));

        verify(productService, times(1)).updateProduct(eq(productId), any(CreateProductRequest.class));
    }

    @Test
    @DisplayName("Should delete product successfully")
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(productId);

        mockMvc.perform(delete("/api/v1/products/{id}", productId))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(productId);
    }
}