package com.tyson.inventory.repository;

import com.tyson.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Search
    List<Product> findByProductNameContainingIgnoreCase(String keyword);

    // Filter by Category
    List<Product> findByCategory(String category);

    // Sort by Price
    List<Product> findAllByOrderByPriceAsc();

    // Sort by Product Name
    List<Product> findAllByOrderByProductNameAsc();

    // Dashboard
    long countByQuantityLessThan(int quantity);

    @Query("SELECT SUM(p.price * p.quantity) FROM Product p")
    Double getTotalInventoryValue();

    @Query("SELECT COUNT(DISTINCT p.category) FROM Product p")
    long countCategories();
}