package org.fjorum.controller.form;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class UserRightsForm {

    public final static String NAME = "userRightsForm";

    @NotNull
    private Long userId = 0L;

    private Set<Long> roleId = new HashSet<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Long> getRoleId() {
        return roleId;
    }

    public void setRoleId(Set<Long> roleId) {
        this.roleId = roleId;
    }
}
