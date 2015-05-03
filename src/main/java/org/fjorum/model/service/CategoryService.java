package org.fjorum.model.service;

import org.fjorum.model.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category createNewCategory(String name);

    void removeCategory(Category category);

    void up(Category category);

    void down(Category category);

    List<Category> findAllCategories();

    Optional<Category> findCategoryById(Long id);
}
