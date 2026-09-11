package com.tyson.inventory.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tyson.inventory.entity.Product;
import com.tyson.inventory.repository.ProductRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Cloudinary cloudinary;


    // =========================
    // SHOW ALL PRODUCTS
    // =========================

    @GetMapping("/products")
    public String showProducts(Model model) {

        model.addAttribute(
                "products",
                productRepository.findAll()
        );

        return "products";
    }


    // =========================
    // SHOW ADD PRODUCT PAGE
    // =========================

    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {

        model.addAttribute("product", new Product());

        return "add-product";
    }


    // =========================
    // ADD / SAVE PRODUCT
    // =========================

    @PostMapping("/save-product")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            @RequestParam(value = "image", required = false)
            MultipartFile image,
            Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            return "add-product";
        }

        // Cloudinary upload
        if (image != null && !image.isEmpty()) {

            Map uploadResult = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "stocksphere/products",
                            "resource_type", "image"
                    )
            );

            String imageUrl =
                    (String) uploadResult.get("secure_url");

            product.setImageName(imageUrl);
        }

        productRepository.save(product);

        return "redirect:/products";
    }
    // =========================
    // SHOW EDIT PRODUCT PAGE
    // =========================

    @GetMapping("/edit-product/{id}")
    public String editProduct(
            @PathVariable Long id,
            Model model) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid product ID: " + id
                        )
                );

        model.addAttribute("product", product);

        return "edit-product";
    }


    // =========================
    // UPDATE PRODUCT
    // =========================

    @PostMapping("/products/update/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            @RequestParam(value = "image", required = false)
            MultipartFile image,
            Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            return "edit-product";
        }

        Product existingProduct =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid product ID: " + id
                                )
                        );

        existingProduct.setProductName(
                product.getProductName()
        );

        existingProduct.setCategory(
                product.getCategory()
        );

        existingProduct.setPrice(
                product.getPrice()
        );

        existingProduct.setQuantity(
                product.getQuantity()
        );


        // Upload new image if selected
        if (image != null && !image.isEmpty()) {

            Map uploadResult =
                    cloudinary.uploader().upload(
                            image.getBytes(),
                            ObjectUtils.asMap(
                                    "folder",
                                    "stocksphere/products",

                                    "resource_type",
                                    "image"
                            )
                    );

            String imageUrl =
                    (String) uploadResult.get("secure_url");

            existingProduct.setImageName(imageUrl);
        }


        // If no new image selected,
        // old image URL remains unchanged.

        productRepository.save(existingProduct);

        return "redirect:/products";
    }
    // =========================
    // DELETE PRODUCT
    // =========================

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(
            @PathVariable Long id) {

        productRepository.deleteById(id);

        return "redirect:/products";
    }

}