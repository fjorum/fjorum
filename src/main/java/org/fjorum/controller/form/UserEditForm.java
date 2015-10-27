package org.fjorum.controller.form;

import org.fjorum.model.entity.User;
import org.hibernate.validator.constraints.NotEmpty;

public class UserEditForm {

    public final static String NAME = "userEditForm";

    @NotEmpty
    private User editedUser;

    @NotEmpty
    private String name = "";

    @NotEmpty
    private String email = "";

    private boolean active = true;

    UserEditForm() {}

    public UserEditForm(User editedUser) {
        this.editedUser = editedUser;
        this.name = editedUser.getName();
        this.email = editedUser.getEmail();
        this.active = editedUser.isActive();
    }

    public User getEditedUser() {
        return editedUser;
    }

    public void setEditedUser(User editedUser) {
        this.editedUser = editedUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
