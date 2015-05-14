package org.fjorum;

import org.fjorum.controller.form.UserCreateForm;
import org.fjorum.model.entity.Role;
import org.fjorum.model.entity.User;
import org.fjorum.model.service.PermissionService;
import org.fjorum.model.service.RoleService;
import org.fjorum.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@Scope("singleton")
@Profile("development")
public class DbInitializer {

    private final RoleService roleService;
    private final UserService userService;
    private final PermissionService permissionService;

    private enum ROLE {ROLE_USER, ROLE_MODERATOR, ROLE_ADMINISTRATOR, ROLE_OWNER, ROLE_GUEST}

    @Autowired
    public DbInitializer(RoleService roleService, UserService userService, PermissionService permissionService) {
        this.roleService = roleService;
        this.userService = userService;
        this.permissionService = permissionService;
        if (roleService.getAllRoles().isEmpty()) {
            initRoles();
        }
        if (userService.getAllUsers().isEmpty()) {
            initOwner();
        }
    }

    private void initRoles() {
        for (ROLE roleEnum : ROLE.values()) {
            String roleName = roleEnum.name();
            String roleKey = roleName.replace('_', '.').toLowerCase();
            Role role = new Role(roleName, roleKey, true);
            if (roleEnum.equals(ROLE.ROLE_OWNER)) {
                role.getPermissions().addAll(
                        permissionService.getAllPermissions());
            }
            roleService.addRole(role);
        }
    }

    private void initOwner() {
        UserCreateForm form = new UserCreateForm();
        form.setName("admin");
        form.setEmail("admin@foo.bar");
        form.setPassword("admin");
        form.setPasswordRepeated("admin");

        User user = userService.create(form);

        Role owner_role = roleService.
                findRole(ROLE.ROLE_OWNER.name()).
                orElseThrow(RuntimeException::new);
        user.getRoles().add(owner_role);
        userService.save(user);
    }
}
