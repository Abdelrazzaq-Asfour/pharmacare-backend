package com.pharmacare.service.impl;

import com.pharmacare.model.Supplier;
import com.pharmacare.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierServiceImpl {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public Supplier createSupplier(Supplier supplier) {
        if (supplier.getSupplierName() == null || supplier.getSupplierName().trim().isEmpty()) {
            throw new RuntimeException("Supplier name is required.");
        }
        if (supplier.getPhone() == null || supplier.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Supplier phone number is required.");
        }

        if (supplierRepository.existsByPhone(supplier.getPhone())) {
            throw new RuntimeException("A supplier with this phone number already exists.");
        }

        if (supplier.getEmail() != null && !supplier.getEmail().trim().isEmpty()) {
            if (supplierRepository.existsByEmail(supplier.getEmail())) {
                throw new RuntimeException("A supplier with this email address already exists.");
            }
        }

        return supplierRepository.save(supplier);
    }

    @Transactional(rollbackFor = Exception.class)
    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with ID: " + id));

        // التحقق من عدم تكرار الهاتف فقط إذا تم تغييره
        if (updatedSupplier.getPhone() != null && !updatedSupplier.getPhone().trim().isEmpty()) {
            if (!updatedSupplier.getPhone().equals(supplier.getPhone())) {
                List<Supplier> existingByPhoneList = supplierRepository.findByPhone(updatedSupplier.getPhone());
                for (Supplier existing : existingByPhoneList) {
                    if (!existing.getSupplierId().equals(id)) {
                        throw new RuntimeException("Another supplier is already using this phone number.");
                    }
                }
            }
            supplier.setPhone(updatedSupplier.getPhone());
        }

        if (updatedSupplier.getEmail() != null && !updatedSupplier.getEmail().trim().isEmpty()) {
            if (!updatedSupplier.getEmail().equals(supplier.getEmail())) {
                List<Supplier> existingByEmailList = supplierRepository.findByEmail(updatedSupplier.getEmail());
                for (Supplier existing : existingByEmailList) {
                    if (!existing.getSupplierId().equals(id)) {
                        throw new RuntimeException("Another supplier is already using this email address.");
                    }
                }
            }
            supplier.setEmail(updatedSupplier.getEmail());
        } else {
            supplier.setEmail(null);
        }

        if (updatedSupplier.getSupplierName() != null) {
            supplier.setSupplierName(updatedSupplier.getSupplierName());
        }
        if (updatedSupplier.getContactPerson() != null) {
            supplier.setContactPerson(updatedSupplier.getContactPerson());
        }
        if (updatedSupplier.getAddress() != null) {
            supplier.setAddress(updatedSupplier.getAddress());
        }

        return supplierRepository.save(supplier);
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Supplier not found with ID: " + id);
        }
        supplierRepository.deleteById(id);
    }


}