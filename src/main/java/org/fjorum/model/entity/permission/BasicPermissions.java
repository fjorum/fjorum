package org.fjorum.model.entity.permission;

import com.google.common.collect.Lists;
import org.fjorum.model.entity.Permission;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class BasicPermissions implements PermissionProvider {

    public enum BasicPermission implements Permission {
        ACCESS_MODERATION_PAGE("permission.accessModerationPage"),
        ACCESS_ADMINISTRATION_PAGE("permission.accessAdministrationPage"),
        ADMINISTRATE_USERS("permission.administrateUsers");

        private String descriptionKey;

        BasicPermission(String descriptionKey) {
            this.descriptionKey = descriptionKey;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getDescriptionKey() {
            return descriptionKey;
        }
    }

    @Override
    public Collection<Permission> getPermissions() {
        return Lists.newArrayList(BasicPermission.values());
    }
}
