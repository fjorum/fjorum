package org.fjorum.model.service;

import org.fjorum.model.entity.Permission;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public interface PermissionService {
    Optional<Permission> getPermission(String name);
    Collection<Permission> getAllPermissions();
}
