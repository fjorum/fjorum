package org.fjorum.model.service;

import org.fjorum.model.entity.Permission;
import org.fjorum.model.entity.permission.PermissionConverter;
import org.fjorum.model.entity.permission.PermissionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class PermissionServiceImpl implements PermissionService {

    public final Set<PermissionProvider> permissionProviders;

    @Autowired
    public PermissionServiceImpl(Set<PermissionProvider> permissionProviders) {
        this.permissionProviders = permissionProviders;
    }

    @Override
    public Optional<Permission> getPermission(String name) {
        return getAllPermissions().stream().
                filter(p -> p.getName().equals(name)).
                findFirst();
    }

    @Override
    public Collection<Permission> getAllPermissions() {
        return permissionProviders.stream().
                flatMap(provider -> provider.getPermissions().stream()).
                distinct().
                sorted(comparing(Permission::getName)).
                collect(toList());
    }
}
