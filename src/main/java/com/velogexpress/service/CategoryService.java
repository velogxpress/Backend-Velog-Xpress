package com.velogexpress.service;

import com.velogexpress.model.CategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CategoryService {
    CategoryModel createCategory(CategoryModel categoryModel);
    Page<CategoryModel> getAllCategory(Pageable pageable);
    CategoryModel getCategoryByDescription(String description);
    CategoryModel getCategoryByID(Long id);
    Page<CategoryModel> getCategoryByAllDescription(String description, Pageable pageable);
    Page<CategoryModel> getCategoryByPart(String description, Pageable pageable);
    CategoryModel updateCategory(Long id,CategoryModel categoryModel);
    void deleteCategory(Long id);
}
