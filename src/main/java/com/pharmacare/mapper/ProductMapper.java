package com.pharmacare.mapper;

import com.pharmacare.dto.request.CreateProductRequest;
import com.pharmacare.dto.response.ProductResponse;
import com.pharmacare.model.Product;
import org.springframework.stereotype.Component;

/**
 * Manual mapper for Product entities and DTOs (Boxes Only). Fast, predictable, and zero-magic.
 */
@Component
public class ProductMapper {

    public Product toEntity(CreateProductRequest request) {
        if (request == null) {
            return null;
        }
        Product product = new Product();
        product.setTradeName(request.getTradeName());
        product.setScientificName(request.getScientificName());
        product.setCategory(request.getCategory());
        product.setSellingPricePerBox(request.getSellingPricePerBox());
        product.setMinStockAlertThreshold(request.getMinStockAlertThreshold());
        product.setActive(true);
        return product;
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setTradeName(product.getTradeName());
        response.setScientificName(product.getScientificName());
        response.setCategory(product.getCategory());
        response.setSellingPricePerBox(product.getSellingPricePerBox());
        response.setMinStockAlertThreshold(product.getMinStockAlertThreshold());
        response.setActive(product.isActive());
        return response;
    }
}