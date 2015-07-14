package org.fjorum.model.service;

import org.fjorum.controller.form.CategoryCreateForm;
import org.fjorum.model.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService extends EntityService<Category> {

    String ROOT = "__ROOT__";

    Category createNewCategory(String name, Category parent);

    Category createNewCategory(CategoryCreateForm form);

    Category getRootCategory();

    void up(Category category);

    void down(Category category);


}
