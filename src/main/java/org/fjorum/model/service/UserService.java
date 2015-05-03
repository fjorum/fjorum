package org.fjorum.model.service;

import org.fjorum.controller.form.UserCreateForm;
import org.fjorum.model.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByNameOrEmail(String nameOremail);

    Collection<User> getAllUsers();

    User create(UserCreateForm form);

}
