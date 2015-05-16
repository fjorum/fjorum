package org.fjorum.model.service;

import org.fjorum.model.entity.Permission;
import org.fjorum.model.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(),
                user.getPasswordHash(),
                user.isActive(),
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList(getRolesAndPermissions(user)));
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

    public Set<String> getRolesAndPermissions() {
        return user.getRoles().stream().
                flatMap(role -> concat(of(role.getName()),
                        role.getPermissions().stream().map(Permission::getName))).
                collect(Collectors.toSet());
    }

    public boolean hasRoleOrPermission(String name) {
        return user.getRoles().stream().
                flatMap(role -> concat(of(role.getName()),
                        role.getPermissions().stream().map(Permission::getName))).
                anyMatch(s -> s.equals(name));
    }

    private static String[] getRolesAndPermissions(User user) {
        return user.getRoles().stream().flatMap(role -> concat(of(role.getName()),
                        role.getPermissions().stream().map(Permission::getName))
        ).distinct().toArray(String[]::new);
    }

}
