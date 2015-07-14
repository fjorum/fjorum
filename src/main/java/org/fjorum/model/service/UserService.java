package org.fjorum.model.service;

import org.fjorum.controller.form.UserCreateForm;
import org.fjorum.controller.form.UserRightsForm;
import org.fjorum.model.entity.User;

import java.util.Optional;

public interface UserService extends EntityService<User> {

    Optional<User> getUserByNameOrEmail(String nameOrEmail);

    User create(UserCreateForm form);

    void changeRights(UserRightsForm form);

}
