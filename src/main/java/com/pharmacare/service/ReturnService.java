package com.pharmacare.service;

import com.pharmacare.dto.request.ReturnRequestActionDto;

public interface ReturnService {
    void requestReturn(ReturnRequestActionDto request, String requesterUsername);
    void resolveReturnRequest(Long returnRequestId, boolean approved, String adminUsername);
}