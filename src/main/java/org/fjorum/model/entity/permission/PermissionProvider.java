package org.fjorum.model.entity.permission;

import org.fjorum.model.entity.Permission;

import java.util.Collection;

/**
 * Defines a set of permissions.
 *
 * If you want to add your own permissions to the application (e.g. for a plugin), write a component
 * implementing this interface, so the PermissionService implementation can get it autowired.
 */
public interface PermissionProvider {
    Collection<Permission> getPermissions();
}
