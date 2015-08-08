package org.fjorum;

import org.fjorum.controller.form.UserCreateForm;
import org.fjorum.model.entity.Category;
import org.fjorum.model.entity.Role;
import org.fjorum.model.entity.User;
import org.fjorum.model.service.CategoryService;
import org.fjorum.model.service.RoleService;
import org.fjorum.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Lazy(false)
@Scope("singleton")
@Profile("development")
public class DbInitializer {

    private enum ROLE {
        ACCESS_MODERATION_PAGE,
        ACCESS_ADMINISTRATION_PAGE,
        ADMINISTRATE_USERS,
        ROLE_USER(),
        ROLE_MODERATOR(ACCESS_MODERATION_PAGE, ADMINISTRATE_USERS),
        ROLE_ADMINISTRATOR(ROLE_MODERATOR, ACCESS_ADMINISTRATION_PAGE),
        ROLE_OWNER(ROLE_ADMINISTRATOR),
        ROLE_GUEST();
        public final ROLE[] includesRoles;
        ROLE(ROLE... includesRoles) {
           this.includesRoles = includesRoles;
        }
    }

    @Autowired
    public DbInitializer(RoleService roleService,
                         UserService userService,
                         CategoryService categoryService) {
        if (roleService.getAllRoles().isEmpty()) {
            initRoles(roleService);
        }
        if (userService.getAll().isEmpty()) {
            initOwner(userService, roleService);
        }
        if (categoryService.getRootCategory() == null) {
            initCategory(categoryService);
        }
    }

    private void initRoles(RoleService roleService) {
        Map<String, Role> roles = new HashMap<>();
        for (ROLE roleEnum : ROLE.values()) {
            String roleName = roleEnum.name();
            String roleKey = roleName.replace('_', '.').toLowerCase();
            Role role = new Role(roleName, roleKey, true);
            role.setIncludedRoles(Stream.of(roleEnum.includesRoles)
                    .map(e -> roles.get(e.name()))
                    .collect(Collectors.toSet())
            );
            roles.put(roleName, roleService.addRole(role));
        }
    }

    private void initOwner(UserService userService, RoleService roleService) {
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

    private void initCategory(CategoryService categoryService) {
        Category root = new Category(CategoryService.ROOT, null);
        categoryService.save(root);
    }

}
