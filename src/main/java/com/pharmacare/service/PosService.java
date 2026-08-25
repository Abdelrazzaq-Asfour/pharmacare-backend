package com.pharmacare.service;

import com.pharmacare.dto.request.CreateInvoiceRequest;
import com.pharmacare.dto.response.InvoiceResponse;

import java.util.List;

public interface PosService {
    InvoiceResponse processSale(CreateInvoiceRequest request, String currentUsername);
    List<InvoiceResponse> getAllInvoices();
}