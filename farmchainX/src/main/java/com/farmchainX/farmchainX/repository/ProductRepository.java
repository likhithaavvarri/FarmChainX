package com.farmchainX.farmchainX.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmchainX.farmchainX.model.Product;
import com.farmchainX.farmchainX.model.User;

public interface ProductRepository extends JpaRepository<Product, Long> {
 
	List<Product> findByFarmer(User farmer);
}
