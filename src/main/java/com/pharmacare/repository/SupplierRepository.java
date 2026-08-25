package com.pharmacare.repository;

import com.pharmacare.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for pharmaceutical suppliers and vendors.
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {



    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);

    List<Supplier> findByPhone(String phone);
    List<Supplier> findByEmail(String email);
}