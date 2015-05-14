package org.fjorum.model.service;

import org.fjorum.model.entity.Role;

import java.util.Collection;
import java.util.Optional;

public interface RoleService {
    Collection<Role> getAllRoles();

    void addRole(Role role);

    Optional<Role> findRole(String name);
}
