package com.tyson.inventory.repository;

import com.tyson.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // ==========================
    // Search
    // ==========================
    List<Product> findByProductNameContainingIgnoreCase(String keyword);
    List<Product> findByQuantityLessThan(int quantity);
    List<Product> findTop5ByOrderByIdDesc();

    // ==========================
    // Category Filter
    // Ignore Case
    // ==========================
    List<Product> findByCategoryIgnoreCase(String category);

    // ==========================
    // Sorting
    // ==========================
    List<Product> findAllByOrderByProductNameAsc();

    List<Product> findAllByOrderByPriceAsc();

    // ==========================
    // Pagination
    // ==========================
    Page<Product> findAll(Pageable pageable);

    // ==========================
    // Dashboard
    // ==========================
    long countByQuantityLessThan(int quantity);

    @Query("SELECT COALESCE(SUM(p.price * p.quantity),0) FROM Product p")
    Double getTotalInventoryValue();

    @Query("SELECT COUNT(DISTINCT p.category) FROM Product p")
    long countCategories();
}