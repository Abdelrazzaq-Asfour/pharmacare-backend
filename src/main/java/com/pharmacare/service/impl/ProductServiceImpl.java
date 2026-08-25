package com.pharmacare.service.impl;

import com.pharmacare.dto.request.CreateProductRequest;
import com.pharmacare.dto.response.ProductResponse;
import com.pharmacare.mapper.ProductMapper;
import com.pharmacare.model.Product;
import com.pharmacare.repository.ProductBatchRepository;
import com.pharmacare.repository.ProductRepository;
import com.pharmacare.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages product definitions, catalog searching, and pricing updates.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductBatchRepository productBatchRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, ProductBatchRepository productBatchRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productBatchRepository = productBatchRepository;
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setTradeName(request.getTradeName());
        product.setScientificName(request.getScientificName());
        product.setCategory(request.getCategory());

        product.setSellingPricePerBox(request.getSellingPricePerBox());

        product.setMinStockAlertThreshold(request.getMinStockAlertThreshold());
        product.setActive(true);

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse updateProductPrice(Long productId, BigDecimal newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        product.setSellingPricePerBox(newPrice);
        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }
    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, CreateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        product.setTradeName(request.getTradeName());
        product.setScientificName(request.getScientificName());
        product.setCategory(request.getCategory());
        product.setSellingPricePerBox(request.getSellingPricePerBox());
        product.setMinStockAlertThreshold(request.getMinStockAlertThreshold());

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));


        boolean hasBatches = productBatchRepository.existsByProduct_ProductId(id);

        if (hasBatches) {

            product.setActive(false);
            productRepository.save(product);
        } else {

            productRepository.delete(product);
        }
    }
}