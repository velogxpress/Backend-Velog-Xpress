package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

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
    @CacheEvict(cacheNames = "category", allEntries = true)
    @PostMapping
    public ResponseEntity<CategoryModel> createCategory(@RequestBody CategoryModel categoryModel) {
        CategoryModel category = categoryService.createCategory(categoryModel);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    // Get All Categories
    @Cacheable(cacheNames = "category")
    @GetMapping
    public ResponseEntity<Page<CategoryModel>> getAllCategories(Pageable pageable) {
        Page<CategoryModel> categoryModelList = categoryService.getAllCategory(pageable);
        return ResponseEntity.ok(categoryModelList);
    }

    // Search Category by Description
    @Cacheable(cacheNames = "category")
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
    @Cacheable(cacheNames = "category")
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
    @CacheEvict(cacheNames = "category", allEntries = true)
    @PutMapping("{id}")
    public ResponseEntity<CategoryModel> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryModel categoryModel
    ) {
        CategoryModel updatedCategory = categoryService.updateCategory(id, categoryModel);
        return ResponseEntity.ok(updatedCategory);
    }

    // Delete Category
    @CacheEvict(cacheNames = "category", allEntries = true)
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category Deleted Successfully.");
    }

    private CategoryService categoryServices;
    //Build Get Category By description
    @Cacheable(cacheNames = "category")
    @GetMapping("/category/{description}")
    public ResponseEntity<CategoryModel> getCategoryByDescription(@PathVariable("description") String description){
        CategoryModel categoryModel=categoryService.getCategoryByDescription(description);
        return ResponseEntity.ok(categoryModel);
    }

    private CategoryService categoryServicess;
    @Cacheable(cacheNames = "category")
    @GetMapping("/categoryByID/{id}")
    public ResponseEntity<CategoryModel> getVilleById(@PathVariable("id") Long villeId){
        CategoryModel categoryModel=categoryService.getCategoryByID(villeId);
        return  ResponseEntity.ok(categoryModel);
    }
}
