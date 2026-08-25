package com.pharmacare.controller;

import com.pharmacare.dto.request.CreateInvoiceRequest;
import com.pharmacare.dto.response.InvoiceResponse;
import com.pharmacare.service.PosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Point of Sale (POS) sales processing and checkout execution.
 */
@RestController
@RequestMapping("/api/v1/pos")
public class PosController {

    private final PosService posService;

    public PosController(PosService posService) {
        this.posService = posService;
    }

    @PostMapping("/sale")
    public ResponseEntity<InvoiceResponse> processSale(@Valid @RequestBody CreateInvoiceRequest request,
                                                       Authentication authentication) {
        String currentUsername = (authentication != null) ? authentication.getName() : "admin_alex";
        InvoiceResponse response = posService.processSale(request, currentUsername);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(posService.getAllInvoices());
    }
}