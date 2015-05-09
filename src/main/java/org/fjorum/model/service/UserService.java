package org.fjorum.model.service;

import org.fjorum.controller.form.UserCreateForm;
import org.fjorum.model.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByNameOrEmail(String nameOrEmail);

    Collection<User> getAllUsers();

    User create(UserCreateForm form);

}
