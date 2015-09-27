package org.fjorum.model.service;


import org.fjorum.controller.form.CategoryCreateForm;
import org.fjorum.model.entity.Category;
import org.fjorum.model.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends AbstractEntityServiceImpl<Category> implements CategoryService {

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
        return createNewCategory(form.getCategoryName(), form.getParentCategory());
    }

    @Override
    public Category getRootCategory() {
        return categoryRepository.getRoot();
    }


    @Override
    protected JpaRepository<Category, Long> getRepository() {
        return categoryRepository;
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
}
