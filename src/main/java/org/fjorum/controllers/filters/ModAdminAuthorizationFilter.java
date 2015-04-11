package org.fjorum.controllers.filters;


import ninja.Filter;
import org.fjorum.models.User;

import static org.fjorum.util.Predicates.and;
import static org.fjorum.util.Predicates.or;

public class ModAdminAuthorizationFilter extends AbstractUserFilter implements Filter {

    public ModAdminAuthorizationFilter() {
        super(and(User::isActive, or(User::isAdministrator, User::isModerator)),
                "security.noModAdminAuthorization");
    }

}

