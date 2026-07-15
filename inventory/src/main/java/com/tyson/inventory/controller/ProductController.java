package com.tyson.inventory.controller;


import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.tyson.inventory.service.PdfService;
import com.tyson.inventory.entity.Product;
import com.tyson.inventory.repository.ProductRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.io.File;
import java.io.IOException;


@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final PdfService pdfService;


    public ProductController(ProductRepository productRepository,
                             PdfService pdfService) {

        this.productRepository = productRepository;
        this.pdfService = pdfService;

    }

    // ==========================
    // Product List + Pagination
    // ==========================
    @GetMapping("/products")
    public String viewProducts(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 5);

        Page<Product> productPage = productRepository.findAll(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "products";
    }

    // ==========================
    // Search
    // ==========================
    @GetMapping("/search")
    public String searchProduct(
            @RequestParam String keyword,
            Model model) {

        model.addAttribute(
                "products",
                productRepository.findByProductNameContainingIgnoreCase(keyword)
        );

        return "products";
    }

    // ==========================
    // Category Filter
    // ==========================
    @GetMapping("/category")
    public String filterCategory(
            @RequestParam String category,
            Model model) {

        model.addAttribute(
                "products",
                productRepository.findByCategoryIgnoreCase(category)
        );

        return "products";
    }
@GetMapping("/export/pdf")
public ResponseEntity<byte[]> exportPdf() {

    List<Product> products = productRepository.findAll();

    byte[] pdf = pdfService.generateProductPdf(products);

    return ResponseEntity.ok()
            .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=products.pdf"
            )
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
}

    // ==========================
    // Sort by Name
    // ==========================
    @GetMapping("/sort/name")
    public String sortByName(Model model) {

        model.addAttribute(
                "products",
                productRepository.findAllByOrderByProductNameAsc()
        );

        return "products";
    }

    // ==========================
    // Sort by Price
    // ==========================
    @GetMapping("/sort/price")
    public String sortByPrice(Model model) {

        model.addAttribute(
                "products",
                productRepository.findAllByOrderByPriceAsc()
        );

        return "products";
    }

    // ==========================
    // Add Product Page
    // ==========================
    @GetMapping("/add-product")
    public String addProductPage(Model model) {

        model.addAttribute("product", new Product());

        return "add-product";
    }
    // ==========================
    // Save Product
    // ==========================
    @PostMapping("/save-product")
    public String saveProduct(
            @Valid @ModelAttribute Product product,
            BindingResult result,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes redirectAttributes,
            Model model) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "add-product";
        }

        boolean isNew = (product.getId() == null);

        // Preserve old image while editing
        if (!isNew) {
            Product oldProduct = productRepository.findById(product.getId()).orElse(null);

            if (oldProduct != null && file.isEmpty()) {
                product.setImageName(oldProduct.getImageName());
            }
        }

        // Upload new image
        if (!file.isEmpty()) {

            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";

            File directory = new File(uploadDir);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            file.transferTo(new File(directory, fileName));

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

    // ==========================
    // Edit Product
    // ==========================
    @GetMapping("/edit-product/{id}")
    public String editProduct(
            @PathVariable Long id,
            Model model) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);

        return "add-product";
    }

    // ==========================
    // Delete Product
    // ==========================
    @GetMapping("/delete-product/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        productRepository.deleteById(id);

        redirectAttributes.addFlashAttribute(
                "success",
                "Product deleted successfully."
        );

        return "redirect:/products";
    }

}