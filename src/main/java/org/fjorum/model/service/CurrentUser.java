package org.fjorum.model.service;

import org.fjorum.model.entity.Role;
import org.fjorum.model.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Set;
import java.util.stream.Collectors;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(),
                user.getPasswordHash(),
                user.isActive(),
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList(getRoles(user)));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getName() {
        return user.getName();
    }

    public Set<String> getRoles() {
        return user.getRoles().stream()
                .flatMap(role -> role.getAllRoles().stream())
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    public boolean hasRole(String name) {
        return getRoles().stream()
                .anyMatch(roleName -> roleName.equals(name));
    }

    private static String[] getRoles(User user) {
        return user.getRoles().stream()
                .flatMap(role -> role.getAllRoles().stream())
                .map(Role::getName)
                .distinct().toArray(String[]::new);
    }

}
