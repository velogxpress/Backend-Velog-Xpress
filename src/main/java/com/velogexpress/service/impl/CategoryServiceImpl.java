package com.velogexpress.service.impl;

import com.velogexpress.entity.Category;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.CategoryMapper;
import com.velogexpress.model.CategoryModel;
import com.velogexpress.repository.CategoryRepository;
import com.velogexpress.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    @Override
    public CategoryModel createCategory(CategoryModel categoryModel) {
        Category category= CategoryMapper.mapToCategory(categoryModel);
        Category saveobj=categoryRepository.save(category);
        return CategoryMapper.mapToCategoryModel(saveobj);
    }

    @Override
    public Page<CategoryModel> getAllCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(CategoryMapper::mapToCategoryModel);
    }

    @Override
    public CategoryModel getCategoryByDescription(String description) {
        Category category=categoryRepository.findByDescription(description);
        if(category!=null){
            return CategoryMapper.mapToCategoryModel(category);
        }else{
            return null;
        }

    }

    @Override
    public CategoryModel getCategoryByID(Long id) {
        Category category=categoryRepository.findById(id)
                .orElseThrow(()->new RessourceNotFoundException("Categorie is not exists with the given id "+id));
        if(category!=null){
            return CategoryMapper.mapToCategoryModel(category);
        }else{
            return null;
        }
    }

    @Override
    public Page<CategoryModel> getCategoryByAllDescription(String description, Pageable pageable) {
        return categoryRepository.search(description, pageable)
                .map(CategoryMapper::mapToCategoryModel);
    }

    @Override
    public Page<CategoryModel> getCategoryByPart(String description, Pageable pageable) {
        return categoryRepository.searchByPart(description, pageable)
                .map(CategoryMapper::mapToCategoryModel);
    }

    @Override
    public CategoryModel updateCategory(Long id, CategoryModel categoryModel) {
       Category category=categoryRepository.findById(id)
               .orElseThrow(()->new RessourceNotFoundException("Category not exists with given id "+id));
       category.setDescription(categoryModel.getDescription());
        category.setPart(categoryModel.getPart());
       Category saveobj=categoryRepository.save(category);
       return CategoryMapper.mapToCategoryModel(saveobj);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category=categoryRepository.findById(id)
                .orElseThrow(()->new RessourceNotFoundException("Category not exists with given id "+id));
        categoryRepository.delete(category);
    }
}
