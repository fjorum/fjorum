package org.fjorum.model.entity.permission;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class BasicPermissions  {

    public enum BasicPermission  {
        ACCESS_MODERATION_PAGE("permission.accessModerationPage"),
        ACCESS_ADMINISTRATION_PAGE("permission.accessAdministrationPage"),
        ADMINISTRATE_USERS("permission.administrateUsers");

        private String descriptionKey;

        BasicPermission(String descriptionKey) {
            this.descriptionKey = descriptionKey;
        }

        public String getName() {
            return name();
        }

        public String getDescriptionKey() {
            return descriptionKey;
        }
    }

}
