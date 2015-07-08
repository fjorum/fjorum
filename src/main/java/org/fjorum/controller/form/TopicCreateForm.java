package org.fjorum.controller.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class TopicCreateForm {

    public final static String NAME = "topicCreateForm";

    @NotNull
    private Long categoryId = 0L;

    @NotNull
    private Long userId = 0L;

    @NotEmpty
    private String name = "";

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
