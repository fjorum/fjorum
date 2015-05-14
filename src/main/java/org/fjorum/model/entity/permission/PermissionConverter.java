package org.fjorum.model.entity.permission;

import org.fjorum.ApplicationContextProvider;
import org.fjorum.model.entity.Permission;
import org.fjorum.model.service.PermissionService;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PermissionConverter implements AttributeConverter<Permission, String> {

    private PermissionService permissionService;

    private PermissionService getPermissionService() {
        if (permissionService == null) {
            permissionService = ApplicationContextProvider.
                    getApplicationContext().
                    getBean(PermissionService.class);
        }
        return permissionService;
    }

    @Override
    public String convertToDatabaseColumn(Permission permission) {
        return permission.getName();
    }

    @Override
    public Permission convertToEntityAttribute(String s) {
        return getPermissionService().getPermission(s).orElseGet(() -> null);
    }
}
