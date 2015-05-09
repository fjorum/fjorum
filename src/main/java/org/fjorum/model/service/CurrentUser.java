package org.fjorum.model.service;

import org.fjorum.model.entity.Permission;
import org.fjorum.model.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;

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

    private static String[] getRolesAndPermissions(User user) {
        return user.getRoles().stream().flatMap(role -> concat(of(role.getName()),
            role.getPermissions().stream().map(Permission::getName))
        ).distinct().toArray(String[]::new);
    }

}
