package org.fjorum.controller.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class CategoryCreateForm {

    public final static String NAME = "categoryCreateForm";

    @NotNull
    private Long parentId = 0L;

    @NotEmpty
    private String name = "";

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
