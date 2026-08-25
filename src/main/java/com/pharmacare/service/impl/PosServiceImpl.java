package com.pharmacare.service.impl;

import com.pharmacare.dto.request.CreateInvoiceRequest;
import com.pharmacare.dto.response.InvoiceResponse;
import com.pharmacare.mapper.InvoiceMapper;
import com.pharmacare.model.*;
import com.pharmacare.repository.*;
import com.pharmacare.service.PosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Critical POS service implementing strict FIFO batch deduction and transaction integrity (Boxes Only).
 */
@Service
public class PosServiceImpl implements PosService {

    private final ProductRepository productRepository;
    private final ProductBatchRepository batchRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final InvoiceMapper invoiceMapper;

    public PosServiceImpl(ProductRepository productRepository,
                          ProductBatchRepository batchRepository,
                          InvoiceRepository invoiceRepository,
                          UserRepository userRepository,
                          InvoiceMapper invoiceMapper) {
        this.productRepository = productRepository;
        this.batchRepository = batchRepository;
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvoiceResponse processSale(CreateInvoiceRequest request, String currentUsername) {
        User pharmacist = userRepository.findByUsername(currentUsername)
                .orElseGet(() -> userRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new RuntimeException("No users found in database.")));

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setPharmacist(pharmacist);
        invoice.setPaymentMethod(Invoice.PaymentMethod.valueOf(request.getPaymentMethod()));
        invoice.setInvoiceStatus(Invoice.InvoiceStatus.COMPLETED);

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (var itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found ID: " + itemReq.getProductId()));

            int remainingBoxesToSell = itemReq.getQuantityBoxes();

            // Fetch batches sorted by expiry ascending (FIFO principle)
            List<ProductBatch> availableBatches = batchRepository.findAvailableBatchesForFifo(product.getProductId());

            int totalAvailableStock = availableBatches.stream().mapToInt(ProductBatch::getCurrentBoxesQuantity).sum();
            if (totalAvailableStock < remainingBoxesToSell) {
                throw new RuntimeException("Insufficient stock for product: " + product.getTradeName());
            }

            for (ProductBatch batch : availableBatches) {
                if (remainingBoxesToSell <= 0) break;

                int boxesFromThisBatch = Math.min(batch.getCurrentBoxesQuantity(), remainingBoxesToSell);

                // Deduct stock from batch
                batch.setCurrentBoxesQuantity(batch.getCurrentBoxesQuantity() - boxesFromThisBatch);
                batchRepository.save(batch);

                // Create invoice item bound to this specific batch for audit trail
                InvoiceItem item = new InvoiceItem();
                item.setProduct(product);
                item.setBatch(batch);
                item.setQuantityBoxes(boxesFromThisBatch);
                item.setUnitPrice(product.getSellingPricePerBox());

                BigDecimal itemTotal = product.getSellingPricePerBox().multiply(BigDecimal.valueOf(boxesFromThisBatch));
                item.setTotalPrice(itemTotal);

                invoice.addItem(item);
                grandTotal = grandTotal.add(itemTotal);

                remainingBoxesToSell -= boxesFromThisBatch;
            }
        }

        invoice.setTotalAmount(grandTotal);
        Invoice savedInvoice = invoiceRepository.save(invoice);

        return invoiceMapper.toResponse(savedInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream()
                .map(invoiceMapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }
}