package com.tyson.inventory.controller;

import com.tyson.inventory.entity.Product;
import com.tyson.inventory.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public String viewProducts(Model model) {

        model.addAttribute("products", productRepository.findAll());

        return "products";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam String keyword, Model model) {

        model.addAttribute("products",
                productRepository.findByProductNameContainingIgnoreCase(keyword));

        return "products";
    }

    @GetMapping("/category")
    public String filterCategory(@RequestParam String category,
                                 Model model) {

        model.addAttribute("products",
                productRepository.findByCategory(category));

        return "products";
    }

    @GetMapping("/sort/name")
    public String sortName(Model model) {

        model.addAttribute("products",
                productRepository.findAllByOrderByProductNameAsc());

        return "products";
    }

    @GetMapping("/sort/price")
    public String sortPrice(Model model) {

        model.addAttribute("products",
                productRepository.findAllByOrderByPriceAsc());

        return "products";
    }

    @GetMapping("/add-product")
    public String addProductPage(Model model) {

        model.addAttribute("product", new Product());

        return "add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(

            @ModelAttribute Product product,

            @RequestParam("image") MultipartFile file,

            RedirectAttributes redirectAttributes)

            throws IOException {

        boolean isNew = (product.getId() == null);

        if (!file.isEmpty()) {

            String uploadDir = "uploads/";

            File directory = new File(uploadDir);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = file.getOriginalFilename();

            file.transferTo(new File(uploadDir + fileName));

            product.setImageName(fileName);
        }

        productRepository.save(product);

        if (isNew) {

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Product added successfully."
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Product updated successfully."
            );

        }

        return "redirect:/products";
    }

    @GetMapping("/edit-product/{id}")
    public String editProduct(@PathVariable Long id, Model model) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);

        return "add-product";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {

        productRepository.deleteById(id);

        redirectAttributes.addFlashAttribute(
                "success",
                "Product deleted successfully."
        );

        return "redirect:/products";
    }

}