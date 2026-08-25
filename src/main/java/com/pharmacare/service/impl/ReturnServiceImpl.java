package com.pharmacare.service.impl;

import com.pharmacare.dto.request.ReturnRequestActionDto;
import com.pharmacare.model.Invoice;
import com.pharmacare.model.ReturnRequest;
import com.pharmacare.model.User;
import com.pharmacare.repository.InvoiceRepository;
import com.pharmacare.repository.ReturnRequestRepository;
import com.pharmacare.repository.UserRepository;
import com.pharmacare.service.ReturnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Manages return/cancellation workflows requiring Admin authorization.
 */
@Service
public class ReturnServiceImpl implements ReturnService {

    private final ReturnRequestRepository returnRequestRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    public ReturnServiceImpl(ReturnRequestRepository returnRequestRepository,
                             InvoiceRepository invoiceRepository,
                             UserRepository userRepository) {
        this.returnRequestRepository = returnRequestRepository;
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void requestReturn(ReturnRequestActionDto request, String requesterUsername) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        invoice.setInvoiceStatus(Invoice.InvoiceStatus.PENDING_RETURN);
        invoiceRepository.save(invoice);

        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setInvoice(invoice);
        returnRequest.setRequestedBy(requester);
        returnRequest.setStatus(ReturnRequest.RequestStatus.PENDING);
        returnRequest.setReason(request.getReason());

        returnRequestRepository.save(returnRequest);
    }

    @Override
    @Transactional
    public void resolveReturnRequest(Long returnRequestId, boolean approved, String adminUsername) {
        ReturnRequest returnReq = returnRequestRepository.findById(returnRequestId)
                .orElseThrow(() -> new RuntimeException("Return request not found"));
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        Invoice invoice = returnReq.getInvoice();

        if (approved) {
            returnReq.setStatus(ReturnRequest.RequestStatus.APPROVED);
            invoice.setInvoiceStatus(Invoice.InvoiceStatus.RETURNED);
            // Note: In production, reversed quantities can be restored back to batches here safely.
        } else {
            returnReq.setStatus(ReturnRequest.RequestStatus.REJECTED);
            invoice.setInvoiceStatus(Invoice.InvoiceStatus.COMPLETED);
        }

        returnReq.setApprovedBy(admin);
        returnReq.setResolvedAt(LocalDateTime.now());

        returnRequestRepository.save(returnReq);
        invoiceRepository.save(invoice);
    }
}