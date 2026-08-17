package com.velogexpress.controller;

import com.velogexpress.model.CategoryModel;
import com.velogexpress.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // Create Category
    @PostMapping
    public ResponseEntity<CategoryModel> createCategory(@RequestBody CategoryModel categoryModel) {
        CategoryModel category = categoryService.createCategory(categoryModel);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    // Get All Categories
    @GetMapping
    public ResponseEntity<Page<CategoryModel>> getAllCategories(Pageable pageable) {
        Page<CategoryModel> categoryModelList = categoryService.getAllCategory(pageable);
        return ResponseEntity.ok(categoryModelList);
    }

    // Search Category by Description
    @GetMapping("/search/{description}")
    public ResponseEntity<Page<CategoryModel>> getCategoryByDescription(
            @PathVariable String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<CategoryModel> categoryModel = categoryService.getCategoryByAllDescription(description, PageRequest.of(page, size));
        return ResponseEntity.ok(categoryModel);
    }

    // Search Category by Description
    @GetMapping("/searchbypart/{description}")
    public ResponseEntity<Page<CategoryModel>> getCategoryByPart(
            @PathVariable String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<CategoryModel> categoryModel = categoryService.getCategoryByPart(description, PageRequest.of(page, size));
        return ResponseEntity.ok(categoryModel);
    }

    // Update Category
    @PutMapping("{id}")
    public ResponseEntity<CategoryModel> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryModel categoryModel
    ) {
        CategoryModel updatedCategory = categoryService.updateCategory(id, categoryModel);
        return ResponseEntity.ok(updatedCategory);
    }

    // Delete Category
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category Deleted Successfully.");
    }

    private CategoryService categoryServices;
    //Build Get Category By description
    @GetMapping("/category/{description}")
    public ResponseEntity<CategoryModel> getCategoryByDescription(@PathVariable("description") String description){
        CategoryModel categoryModel=categoryService.getCategoryByDescription(description);
        return ResponseEntity.ok(categoryModel);
    }

    private CategoryService categoryServicess;
    @GetMapping("/categoryByID/{id}")
    public ResponseEntity<CategoryModel> getVilleById(@PathVariable("id") Long villeId){
        CategoryModel categoryModel=categoryService.getCategoryByID(villeId);
        return  ResponseEntity.ok(categoryModel);
    }
}
