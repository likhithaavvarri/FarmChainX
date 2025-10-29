package com.farmchainX.farmchainX.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.farmchainX.farmchainX.model.SupplyChainLog;
import java.util.List;
import java.util.Optional;

public interface SupplyChainLogRepository extends JpaRepository<SupplyChainLog, Long> {

  
    List<SupplyChainLog> findByProductIdOrderByTimestampAsc(Long productId);


    Optional<SupplyChainLog> findTopByProductIdOrderByTimestampDesc(Long productId);
}