package org.fjorum.model.service;

import java.util.List;
import java.util.Optional;

import org.fjorum.model.entity.Role;

public interface RoleService {
    List<Role> getAllRoles();

    void addRole(Role role);

    Optional<Role> findRole(String name);
}
