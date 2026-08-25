package com.pharmacare.service;

import com.pharmacare.dto.request.CreateProductRequest;
import com.pharmacare.dto.response.ProductResponse;
import java.util.List;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
    List<ProductResponse> searchProducts(String keyword);
    ProductResponse updateProductPrice(Long productId, java.math.BigDecimal newPrice);
    ProductResponse updateProduct(Long productId, CreateProductRequest request);

    void deleteProduct(Long productId);
}