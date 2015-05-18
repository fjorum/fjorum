package org.fjorum.model.service;

import java.util.List;
import java.util.Optional;

import org.fjorum.controller.form.UserCreateForm;
import org.fjorum.model.entity.User;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByNameOrEmail(String nameOrEmail);

    List<User> getAllUsers();

    User create(UserCreateForm form);

    void save(User user);

}
