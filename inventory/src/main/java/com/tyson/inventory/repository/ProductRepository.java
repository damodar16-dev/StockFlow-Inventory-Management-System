package com.tyson.inventory.repository;

import org.springframework.data.jpa.repository.Query;
import com.tyson.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    long countByQuantityLessThan(int quantity);

    @Query("SELECT SUM(p.price * p.quantity) FROM Product p")
    Double getTotalInventoryValue();

    @Query("SELECT COUNT(DISTINCT p.category) FROM Product p")
    long countCategories();
    List<Product> findByProductNameContainingIgnoreCase(String keyword);

}