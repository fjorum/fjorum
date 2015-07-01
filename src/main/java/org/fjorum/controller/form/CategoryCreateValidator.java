package org.fjorum.controller.form;

import org.fjorum.model.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryCreateValidator implements Validator {

    private final CategoryService categoryService;

    @Autowired
    public CategoryCreateValidator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryCreateForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryCreateForm form = (CategoryCreateForm) target;
        categoryService.findCategoryById(form.getParentId()).<Runnable>map(parent -> () -> {
                    if (parent.getChildren().stream().anyMatch(c -> c.getName().equals(form.getName()))) {
                        errors.reject("category.nameExists", "There is already a category with the same name");
                    }
                }
        ).orElse(() -> errors.reject("category.noParentFound", "Could not find parent category")
        ).run();
    }
}
