package com.tyson.inventory.controller;

import com.tyson.inventory.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ProductRepository productRepository;

    public DashboardController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @GetMapping("/")
    public String dashboard(Model model) {


        model.addAttribute("totalProducts",
                productRepository.count());


        model.addAttribute("lowStock",
                productRepository.countByQuantityLessThan(10));


        model.addAttribute("categories",
                productRepository.countCategories());


        Double totalValue =
                productRepository.getTotalInventoryValue();


        model.addAttribute("inventoryValue",
                totalValue == null ? 0 : totalValue);


        model.addAttribute("lowStockProducts",
                productRepository.findByQuantityLessThan(10));
        model.addAttribute("recentProducts",
                productRepository.findTop5ByOrderByIdDesc());


        return "dashboard";

    }
}