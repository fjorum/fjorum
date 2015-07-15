package org.fjorum.controller.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class TopicCreateForm {

    @NotNull
    private Long categoryId = 0L;

    @NotEmpty
    private String name = "";

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
