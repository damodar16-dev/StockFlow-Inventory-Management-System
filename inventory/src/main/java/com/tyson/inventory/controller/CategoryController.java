package com.tyson.inventory.controller;

import com.tyson.inventory.entity.Category;
import com.tyson.inventory.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public String viewCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories";
    }

    @GetMapping("/add-category")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "add-category";
    }

    @PostMapping("/save-category")
    public String saveCategory(
            @Valid @ModelAttribute Category category,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "add-category";
        }

        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("success", "Category saved successfully.");
        return "redirect:/categories";
    }

    @GetMapping("/edit-category/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category",
                categoryRepository.findById(id).orElse(new Category()));
        return "add-category";
    }

    @GetMapping("/delete-category/{id}")
    public String deleteCategory(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {

        categoryRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Category deleted successfully.");
        return "redirect:/categories";
    }
}