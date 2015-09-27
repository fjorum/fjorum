package org.fjorum.controller.form;

import org.fjorum.model.entity.Category;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class CategoryCreateForm {

    public final static String NAME = "categoryCreateForm";

    @NotNull
    private Category parentCategory;

    @NotEmpty
    private String categoryName = "";

    CategoryCreateForm(){}

    public CategoryCreateForm(Category parentCategory){
        this.parentCategory = parentCategory;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
