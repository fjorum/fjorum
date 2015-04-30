package org.fjorum.model.service;


import org.fjorum.model.entity.Category;
import org.fjorum.model.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createNewCategory(String name) {
        Category last = categoryRepository.findBelow(Integer.MAX_VALUE);
        Category category = new Category(name);
        category.setSortOrder(last.getSortOrder() + 1);
        categoryRepository.save(category);
        return category;
    }

    public void removeCategory(Category category) {
        categoryRepository.delete(category);
    }

    public void up(Category category) {
        Category smaller = categoryRepository.findBelow(category.getSortOrder());
        if (smaller != null) {
            int temp = smaller.getSortOrder();
            smaller.setSortOrder(category.getSortOrder());
            category.setSortOrder(temp);
            categoryRepository.save(category);
            categoryRepository.save(smaller);
        }
    }

    public void down(Category category) {
        Category bigger = categoryRepository.findAbove(category.getSortOrder());
        if (bigger != null) {
            int temp = bigger.getSortOrder();
            bigger.setSortOrder(category.getSortOrder());
            category.setSortOrder(temp);
            categoryRepository.save(category);
            categoryRepository.save(bigger);
        }
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll(new Sort("sortOrder"));
    }

    public Optional<Category> findCategoryById(Long id) {
        return Optional.ofNullable(categoryRepository.findOne(id));
    }
}
