package org.fjorum.controllers.filters;


import ninja.Filter;
import org.fjorum.models.User;

import static org.fjorum.util.Predicates.and;

public class AdminAuthorizationFilter extends AbstractUserFilter implements Filter {

    public AdminAuthorizationFilter() {
        super(and(User::isActive, User::isAdministrator),
                "security.noAdminAuthorization");
    }
}
