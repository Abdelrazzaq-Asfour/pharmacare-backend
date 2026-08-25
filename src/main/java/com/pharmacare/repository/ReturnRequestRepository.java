package com.pharmacare.repository;

import com.pharmacare.model.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for managing pending and resolved invoice return requests.
 */
@Repository
public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {

    List<ReturnRequest> findByStatus(ReturnRequest.RequestStatus status);
}