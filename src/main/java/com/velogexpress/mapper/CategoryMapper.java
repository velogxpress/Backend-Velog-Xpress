package com.velogexpress.mapper;

import com.velogexpress.entity.Category;
import com.velogexpress.model.CategoryModel;

public class CategoryMapper {
    public static CategoryModel mapToCategoryModel(Category category){
        return new CategoryModel(
                category.getId(),
                category.getDescription(),
                category.getPart()
        );
    }

    public static Category mapToCategory(CategoryModel categoryModel){
        return new Category(
                categoryModel.getId(),
                categoryModel.getDescription(),
                categoryModel.getPart()
        );
    }
}
