package com.pharmacare.mapper;

import com.pharmacare.dto.response.InvoiceResponse;
import com.pharmacare.model.Invoice;
import com.pharmacare.model.InvoiceItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Maps complex Invoice domain trees to flat, client-safe response DTOs (Boxes Only).
 */
@Component
public class InvoiceMapper {

    public InvoiceResponse toResponse(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        InvoiceResponse response = new InvoiceResponse();
        response.setInvoiceId(invoice.getInvoiceId());
        response.setInvoiceNumber(invoice.getInvoiceNumber());

        if (invoice.getPharmacist() != null) {
            response.setPharmacistUsername(invoice.getPharmacist().getUsername());
        }

        response.setTotalAmount(invoice.getTotalAmount());
        response.setPaymentMethod(invoice.getPaymentMethod().name());
        response.setInvoiceStatus(invoice.getInvoiceStatus().name());
        response.setCreatedAt(invoice.getCreatedAt());

        if (invoice.getItems() != null) {
            var itemResponses = invoice.getItems().stream().map(item -> {
                InvoiceResponse.InvoiceItemResponse itemRes = new InvoiceResponse.InvoiceItemResponse();

                // جلب اسم الدواء
                if (item.getProduct() != null) {
                    itemRes.setTradeName(item.getProduct().getTradeName());
                }

                if (item.getBatch() != null) {
                    itemRes.setBatchNumber(item.getBatch().getBatchNumber());
                } else {
                    itemRes.setBatchNumber("N/A");
                }

                itemRes.setQuantityBoxes(item.getQuantityBoxes());
                itemRes.setUnitPrice(item.getUnitPrice());
                itemRes.setTotalPrice(item.getTotalPrice());
                return itemRes;
            }).collect(Collectors.toList());

            response.setItems(itemResponses);
        }

        return response;
    }
}