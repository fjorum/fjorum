package org.fjorum.controller.form;

import org.fjorum.model.entity.Category;
import org.fjorum.model.entity.User;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class TopicCreateForm {

    public static final String NAME = "topicCreateForm";

    @NotEmpty
    private String topicName = "";

    @NotNull
    private User user;

    @NotNull
    private Category category;

    TopicCreateForm() {
    }

    public TopicCreateForm(User user, Category category) {
        this.user = user;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

}
