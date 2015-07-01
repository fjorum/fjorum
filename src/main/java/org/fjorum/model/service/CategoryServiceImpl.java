package org.fjorum.model.service;


import org.fjorum.controller.form.CategoryCreateForm;
import org.fjorum.model.entity.Category;
import org.fjorum.model.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createNewCategory(String name, Category parent) {
        Category category = new Category(name, parent);
        return categoryRepository.save(category);
    }

    @Override
    public Category createNewCategory(CategoryCreateForm form) {
        return findCategoryById(form.getParentId()).
                map(parent -> createNewCategory(form.getName(), parent)).
                orElseThrow(() -> new DataIntegrityViolationException("Parent category not found"));
    }

    @Override
    public Category getRootCategory() {
        return categoryRepository.getRoot();
    }

    @Override
    public void removeCategory(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void up(Category category) {
        /*Category smaller = categoryRepository.findBelow(category.getOrderId());
        if (smaller != null) {
            int temp = smaller.getOrderId();
            smaller.setOrderId(category.getOrderId());
            category.setOrderId(temp);
            categoryRepository.save(category);
            categoryRepository.save(smaller);
        } */
    }

    @Override
    public void down(Category category) {
        /*Category bigger = categoryRepository.findAbove(category.getOrderId());
        if (bigger != null) {
            int temp = bigger.getOrderId();
            bigger.setOrderId(category.getOrderId());
            category.setOrderId(temp);
            categoryRepository.save(category);
            categoryRepository.save(bigger);
        } */
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(new Sort("sortOrder"));
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {
        return Optional.ofNullable(categoryRepository.findOne(id));
    }
}
