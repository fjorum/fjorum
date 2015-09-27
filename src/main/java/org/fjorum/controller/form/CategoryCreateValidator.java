package org.fjorum.controller.form;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryCreateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryCreateForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryCreateForm form = (CategoryCreateForm) target;
        if (form.getParentCategory().getChildren().stream().anyMatch(c ->
                c.getName().equals(form.getCategoryName()))) {
            errors.reject("category.nameExists",
                    "There is already a category with the same name");
        }
    }
}
