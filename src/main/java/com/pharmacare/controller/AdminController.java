package com.pharmacare.controller;

import com.pharmacare.dto.request.ReturnRequestActionDto;
import com.pharmacare.dto.request.RegisterUserDto;
import com.pharmacare.service.ReturnService;
import com.pharmacare.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final ReturnService returnService;

    public AdminController(ReturnService returnService) {
        this.returnService = returnService;
    }

    @PostMapping("/returns/request")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<Void> requestReturn(@Valid @RequestBody ReturnRequestActionDto request,
                                              Authentication authentication) {
        returnService.requestReturn(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/returns/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> resolveReturn(@PathVariable Long id,
                                              @RequestParam boolean approved,
                                              Authentication authentication) {
        returnService.resolveReturnRequest(id, approved, authentication.getName());
        return ResponseEntity.ok().build();
    }
}